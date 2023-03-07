package org.davincischools.leo.server.utils;

import static org.davincischools.leo.server.CommandLineArguments.COMMAND_LINE_ARGUMENTS;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Bytes;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

public class OpenAiUtils {

  private static final Logger log = LogManager.getLogger();

  public static final String GPT_3_5_TURBO_MODEL = "gpt-3.5-turbo";
  public static final URI COMPLETION_URI = URI.create("https://api.openai.com/v1/chat/completions");

  private static Optional<String> openAiKey = Optional.empty();

  // Makes a call to OpenAI. If no key is available, returns an unmodified response.
  public static <T extends Builder> T sendOpenAiRequest(URI uri, Message request, T responseBuilder)
      throws IOException {
    // Get the OpenAI key, check the openAiKeyFile flat and then the OPENAI_API_KEY environment
    // variable.
    if (openAiKey.isEmpty()) {
      if (COMMAND_LINE_ARGUMENTS.openAiKeyFile != null
          && COMMAND_LINE_ARGUMENTS.openAiKeyFile.exists()) {
        openAiKey =
            Optional.of(
                new String(
                    ByteStreams.toByteArray(
                        new FileInputStream(COMMAND_LINE_ARGUMENTS.openAiKeyFile)),
                    StandardCharsets.UTF_8));
      } else if (System.getenv("OPENAI_API_KEY") != null) {
        openAiKey = Optional.of(System.getenv("OPENAI_API_KEY"));
      } else {
        log.atWarn().log("OpenAI key is not available. Calls will just return a default response.");
        return responseBuilder;
      }
    }

    log.atInfo().log("OpenAI Request: " + JsonFormat.printer().print(request));

    ResponseSpec responseSpec =
        WebClient.create()
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey.get())
            .header(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0")
            .header(HttpHeaders.PRAGMA, "No-Cache")
            .header(HttpHeaders.EXPIRES, "0") // I.e., now.
            .bodyValue(JsonFormat.printer().print(request))
            .retrieve();

    // Get response header.
    ResponseEntity<?> headerResponse =
        Objects.requireNonNull(responseSpec.toBodilessEntity().block());
    if (headerResponse.getStatusCode().isError()) {
      throw new HttpClientErrorException(headerResponse.getStatusCode());
    }

    // Stream the response body because the buffer is limited.
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
    byte[] reactBody = Bytes.concat(streamedBytes.toArray(size -> new byte[size][]));

    log.atInfo().log("OpenAI Response: {}.", new String(reactBody, StandardCharsets.UTF_8));

    // Translate the bytes into a proto.
    JsonFormat.parser()
        .ignoringUnknownFields()
        .merge(new String(reactBody, StandardCharsets.UTF_8), responseBuilder);

    return responseBuilder;
  }
}
