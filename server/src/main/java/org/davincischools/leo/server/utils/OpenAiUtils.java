package org.davincischools.leo.server.utils;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.TextFormat;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.daos.Log;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.utils.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.netty.http.client.HttpClient;

@Component
public class OpenAiUtils {

  private static final Logger logger = LogManager.getLogger();

  public static final String OPENAI_API_KEY_PROP_NAME = "openai.api.key";
  public static final String OPENAI_API_URL_PROP_NAME = "openai.api.url";
  public static final String OPENAI_API_KEY_ENV_NAME =
      OPENAI_API_KEY_PROP_NAME.toUpperCase().replaceAll("\\.", "_");
  public static final String GPT_3_5_TURBO_MODEL = "gpt-3.5-turbo";

  private String openAiKey;
  private String openAiUrl;
  private Database db;

  private OpenAiUtils(
      @Value("${" + OPENAI_API_KEY_PROP_NAME + ":}") String openAiKey,
      @Value("${" + OPENAI_API_URL_PROP_NAME + ":}") String openAiUrl,
      @Autowired Environment environment,
      @Autowired Database db)
      throws IOException {
    this.openAiKey = openAiKey;
    this.openAiUrl = openAiUrl;
    this.db = db;
  }

  public Optional<String> getOpenAiKey() {
    return openAiKey.isEmpty() ? Optional.empty() : Optional.of(openAiKey);
  }

  // Makes a call to OpenAI. If no key is available, returns an unmodified response.
  public <T extends Builder<?>> T sendOpenAiRequest(
      Message request, T responseBuilder, Optional<Integer> user_id) throws IOException {
    logger.atInfo().log("OpenAI Request: " + JsonFormat.printer().print(request));
    if (openAiKey.isEmpty()) {
      logger
          .atWarn()
          .log(
              "OpenAI not called due to missing '"
                  + OPENAI_API_KEY_PROP_NAME
                  + "' property and missing '"
                  + OPENAI_API_KEY_ENV_NAME
                  + "' environment variable.");
      return responseBuilder;
    }

    Log logEntry =
        new Log()
            .setCreationTime(Instant.now())
            .setSource(this.getClass().getName())
            .setRequest(TextFormat.printer().printToString(request))
            .setUser(user_id.map(id -> new User().setId(id)).orElse(null));
    try {
      HttpClient client =
          HttpClient.create()
              .responseTimeout(Duration.ofSeconds(120))
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120 * 100)
              .option(ChannelOption.SO_KEEPALIVE, true)
              .doOnConnected(
                  conn ->
                      conn.addHandlerFirst(new ReadTimeoutHandler(120, TimeUnit.SECONDS))
                          .addHandlerFirst(new WriteTimeoutHandler(120, TimeUnit.SECONDS)));
      ResponseSpec responseSpec =
          WebClient.builder()
              .clientConnector(new ReactorClientHttpConnector(client))
              .build()
              .post()
              .uri(URI.create(openAiUrl))
              .contentType(MediaType.APPLICATION_JSON)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
              .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0")
              .header(HttpHeaders.PRAGMA, "No-Cache")
              .header(HttpHeaders.EXPIRES, "0") // I.e., now.
              .bodyValue(JsonFormat.printer().print(request))
              .retrieve();

      // Get response header.
      ResponseEntity<?> responseHeader =
          Objects.requireNonNull(responseSpec.toBodilessEntity().block());
      if (responseHeader.getStatusCode().isError()) {
        throw new HttpClientErrorException(responseHeader.getStatusCode());
      }

      // Stream the response body because the buffer is limited.
      byte[] responseBytes;
      ImmutableList<byte[]> streamedBytes =
          responseSpec
              .bodyToFlux(DataBuffer.class)
              .map(
                  buffer -> {
                    try (InputStream in = buffer.asInputStream(true)) {
                      return in.readAllBytes();
                    } catch (IOException e) {
                      throw new WrappedIOException(e);
                    }
                  })
              .collect(ImmutableList.toImmutableList())
              .block();
      responseBytes = Bytes.concat(Objects.requireNonNull(streamedBytes).toArray(byte[][]::new));

      // Translate the response back into a proto.
      String responseString = new String(responseBytes, StandardCharsets.UTF_8);
      logEntry.setUnprocessedResponse(responseString);
      JsonFormat.parser().ignoringUnknownFields().merge(responseString, responseBuilder);

      logger.atInfo().log("OpenAI Response: " + JsonFormat.printer().print(responseBuilder));
      logEntry.setResponse(TextFormat.printer().printToString(responseBuilder));
      return responseBuilder;
    } catch (Exception e) {
      logger.atError().withThrowable(e).log("OpenAI Error: {}", e.getMessage());
      logEntry.setStatus("ERROR: " + Throwables.getStackTraceAsString(e));
      throw new IOException(e);
    } finally {
      db.getLogRepository().save(logEntry);
    }
  }
}
