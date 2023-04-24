package org.davincischools.leo.server.utils;

import com.google.common.base.Throwables;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.UserX;
import org.davincischools.leo.database.utils.Database;

/**
 * This is an aid for logging processes execution. It is used as:
 *
 * <pre>{@code
 * Database db = ...;
 * ProtoMessage request = ...;
 *
 * try {
 *   // LogUtils will automatically created a log entry and set the request and
 *   // timestamp. So, no need to do this manually.
 *   return LogUtils.executeAndLog(db, Optional.empty(), request,
 *       (input, logEntry) -> {
 *     // LogUtils will record the request and creation time automatically. But,
 *     // you can overwrite those here, if needed.
 *
 *     // This first consumer should actually do the work and return the raw
 *     // response.
 *
 *     return Integer.valueOf(input);
 *   }).andThen(input, logEntry) -> {
 *     // This will only be called if there were no exceptions so far.
 *
 *     // LogUtils will automatically record the timestamp when this method
 *     // is called as the response time in the log entry.
 *
 *     // The second consumer should set the unprocessed_response with the raw
 *     // response from the first consumer. The input should probably be a
 *     // byte array. (But, we used an Integer in this example).
 *     logEntry.setUnprocessedResponse(toByteArray(input));
 *
 *     // This time the input is an Integer, from the previous andThen()
 *     // operation.
 *     return input.floatValue();
 *   }).onError((error, logEntry) -> {
 *     // This will only be called if there was a previous exception.
 *
 *     // LogUtils will automatically record the stack trace, status, and
 *     // last input as the response in the finish() consumer is executed. So,
 *     // no need to set those here.
 *
 *     // Do something here with the input and exception in the error
 *     // parameter. The input will be the output of the last successful
 *     // previous step.
 *     logEntry.setNotes("There was an error.");
 *
 *     // The log isn't written until finish(). So, throwing an exception here
 *     // is okay.
 *   }).finish((error) -> {
 *     // This is called only if finish() was unable to save the log entry.
 *     // Note that an error has already been logged to the console, and the
 *     // processed response will still be returned.
 *   });
 * } catch (ExecutionError e) {
 *   // The log entry was written. Now handle any exception that occurred.
 *   // E.g., return some error value.
 * }</pre>
 */
public class LogUtils {

  private static final Logger log = LogManager.getLogger();

  public enum Status {
    ERROR,
    SUCCESS
  }

  public record Error<R>(R originalRequest, Object lastInput, Throwable throwable) {}

  public static class LogExecutionError extends IOException {
    private static final long serialVersionUID = 1164992147965935071L;

    private final transient Error<?> error;

    public LogExecutionError(Error<?> error) {
      super(error.throwable);
      this.error = error;
    }

    @Nullable
    Error<?> getError() {
      return error;
    }
  }

  public interface LogConsumer<I, O> {

    O accept(I input, Log logEntry) throws Throwable;
  }

  public interface LogErrorConsumer<R> {

    void accept(Error<R> error, Log logEntry) throws Throwable;
  }

  public interface SaveErrorConsumer {

    void accept(Throwable t);
  }

  public static class LogConsumerResponse<R, I> {

    private final Database db;
    private final R originalRequest;
    private final I input;
    private final Log logEntry;
    /**
     * True if we've handled the error with an onError(). It causes any future functionality to be
     * skipped until the final writes the log entry before it returns from finish().
     */
    private final boolean skipToFinish;

    private Error<R> error;

    public LogConsumerResponse(
        Database db,
        R originalRequest,
        I input,
        Log logEntry,
        boolean skipToFinish,
        Error<R> error) {
      this.db = db;
      this.originalRequest = originalRequest;
      this.input = input;
      this.logEntry = logEntry;
      this.skipToFinish = skipToFinish;
      this.error = error;
    }

    /** The chain of andThen() will continue to be called as long as no exceptions have occurred. */
    @CheckReturnValue
    public <O> LogConsumerResponse<R, O> andThen(LogConsumer<I, O> inputConsumer) {
      if (!skipToFinish && error == null) {
        try {
          return new LogConsumerResponse<>(
              db, originalRequest, inputConsumer.accept(input, logEntry), logEntry, false, null);
        } catch (Throwable t) {
          error = new Error<>(originalRequest, input, t);
        }
      }
      return new LogConsumerResponse<>(db, originalRequest, null, logEntry, skipToFinish, error);
    }

    /**
     * When there's an exception, the first onError in the chain will be called with the last
     * successful output value from a previous andThen() (or, otherwise, the initial input to
     * executeAndLog()).
     *
     * <p>Once an onError() is called, no further consumers will be called before finish() writes
     * the log entry and returns the error.
     */
    @CheckReturnValue
    public LogConsumerResponse<R, I> onError(LogErrorConsumer<R> errorConsumer) {
      if (skipToFinish || error == null) {
        return this;
      }

      try {
        errorConsumer.accept(error, logEntry);
      } catch (Throwable t) {
        error =
            new Error<>(
                originalRequest,
                error.lastInput,
                new RuntimeException("Error occurred while processing in the error handler.", t)
                    .initCause(error.throwable));
      }
      return new LogConsumerResponse<>(db, originalRequest, null, logEntry, true, error);
    }

    /**
     * Runs the final consumer if there have been no exceptions.
     *
     * <p>And finally, it will write the log entry to the database. Before doing so, it will set the
     * status, stack trace, and response to the last input if the response is unset.
     *
     * <p>Returns an error if it fails to write the logEntry to the database.
     */
    @CheckReturnValue
    public I finish(SaveErrorConsumer saveErrorConsumer) throws LogExecutionError {
      try {
        if (logEntry.getFinalResponseTime() == null) {
          logEntry.setFinalResponseTime(Instant.now());
        }
        if (logEntry.getFinalResponse() == null) {
          logEntry.setFinalResponse(ioToString(input));
        }
        if (error == null) {
          try {
            logEntry.setStatus(Status.SUCCESS.name());
            db.getLogRepository().save(logEntry);
          } catch (Throwable t) {
            log.atError()
                .withThrowable(t)
                .log(
                    "An error occurred while saving the log entry. Returning the result anyway."
                        + " Request: {}, Response: {}",
                    logEntry.getRequest(),
                    logEntry.getFinalResponse());
            saveErrorConsumer.accept(t);
          }
          return input;
        }
        logEntry.setStatus(Status.ERROR.name());
        logEntry.setStackTrace(Throwables.getStackTraceAsString(error.throwable));
        db.getLogRepository().save(logEntry);
      } catch (Throwable t) {
        log.atError()
            .withThrowable(t)
            .log(
                "An error occurred while saving the log entry. There was also an error during"
                    + " processing. Request: {}, Last Input: {}",
                logEntry.getRequest(),
                logEntry.getFinalResponse());
        saveErrorConsumer.accept(t);
      }
      throw new LogExecutionError(error);
    }

    public I finish() throws LogExecutionError {
      return finish(error -> {});
    }
  }

  @CheckReturnValue
  public static <I, O> LogConsumerResponse<I, O> executeAndLog(
      Database db,
      String operation,
      Optional<Integer> user_id,
      I request,
      LogConsumer<I, O> consumer) {
    LogConsumerResponse<I, O> response =
        new LogConsumerResponse<>(
                db,
                request,
                request,
                new Log()
                    .setCreationTime(Instant.now())
                    .setOperation(operation)
                    .setRequest(ioToString(request))
                    .setUserX(user_id.map(id -> new UserX().setId(id)).orElse(null)),
                false,
                null)
            .andThen(consumer);

    // Assume that the first consumer waited for external resources.
    if (response.logEntry.getInitialResponseTime() == null) {
      response.logEntry.setInitialResponseTime(Instant.now());
    }
    if (response.input instanceof byte[] && response.logEntry.getInitialResponse() == null) {
      response.logEntry.setInitialResponse((byte[]) response.input);
    }

    return response;
  }

  @CheckReturnValue
  public static <I extends Message, O> LogConsumerResponse<I, O> executeAndLog(
      Database db, Optional<Integer> user_id, I request, LogConsumer<I, O> consumer) {
    return executeAndLog(db, request.getClass().getName(), user_id, request, consumer);
  }

  @Nullable
  private static String ioToString(@Nullable Object o) {
    return o instanceof Message
        ? TextFormat.printer().printToString((Message) o)
        : o != null ? o.toString() : null;
  }
}
