package org.davincischools.leo.server.utils;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Bytes;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Component
public class OpenAiUtils {

  private static final Logger logger = LogManager.getLogger();

  public static final String OPENAI_API_KEY_PROP_NAME = "openai.api.key";
  public static final String OPENAI_API_KEY_ENV_NAME =
      OPENAI_API_KEY_PROP_NAME.toUpperCase().replaceAll("\\.", "_");
  public static final String GPT_3_5_TURBO_MODEL = "gpt-3.5-turbo";
  public static final URI COMPLETIONS_URI =
      URI.create("https://api.openai.com/v1/chat/completions");

  @Value("${" + OPENAI_API_KEY_PROP_NAME + ":}")
  private String openAiKey;

  public Optional<String> getOpenAiKey() {
    return openAiKey.isEmpty() ? Optional.empty() : Optional.of(openAiKey);
  }

  // Makes a call to OpenAI. If no key is available, returns an unmodified response.
  public <T extends Builder<?>> T sendOpenAiRequest(URI uri, Message request, T responseBuilder)
      throws IOException, InvalidProtocolBufferException {
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
    ResponseSpec responseSpec =
        WebClient.create()
            .post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
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
    byte[] reactBody;
    try {
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
      reactBody = Bytes.concat(Objects.requireNonNull(streamedBytes).toArray(byte[][]::new));
    } catch (WrappedIOException e) {
      throw e.getCause();
    }

    logger.atInfo().log("OpenAI Response: {}.", new String(reactBody, StandardCharsets.UTF_8));

    // Translate the bytes into a proto.
    JsonFormat.parser()
        .ignoringUnknownFields()
        .merge(new String(reactBody, StandardCharsets.UTF_8), responseBuilder);

    return responseBuilder;
  }
}
