package org.davincischools.leo.server.utils;

import com.fasterxml.jackson.datatype.jdk8.WrappedIOException;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.net.MediaType;
import com.google.common.primitives.Bytes;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

/**
 * This forwards a received Spring server HttpServletRequest to the development React server and
 * then translates the response from the React server back in the Spring servers
 * HttpServletResponse. This includes translating URLs to and from the React server.
 *
 * <p>This assumes that the React server is running on localhost, and that the Spring and React
 * servers are at the same URL paths.
 */
public class HttpServletProxy {

  // A helper to replace the Spring host and port with the React host and port, and vice versa.
  static class URIRewriter {
    private final String springHostPort;
    private final String reactHostPort;

    public URIRewriter(URI springServerUri, int reactPort) {
      this.springHostPort =
          springServerUri.getHost()
              + (springServerUri.getPort() >= 0 ? ":" + springServerUri.getPort() : "");
      this.reactHostPort = "localhost:" + reactPort;
    }

    public String rewriteForReact(String str) {
      return str.replaceAll(springHostPort, reactHostPort);
    }

    public String rewriteForSpring(String str) {
      return str.replaceAll(reactHostPort, springHostPort);
    }
  }

  // Http header name constants.
  private static final ImmutableSet<String> COOKIE_HEADERS =
      ImmutableSet.of(HttpHeaders.SET_COOKIE, HttpHeaders.SET_COOKIE2);
  private static final ImmutableSet<String> HEADERS_THAT_WILL_BE_AUTO_POPULATED =
      ImmutableSet.of(HttpHeaders.ETAG, HttpHeaders.CONTENT_LENGTH);

  // Cookie attribute names that don't have values.
  private static final String SECURE_ATTR = "Secure";
  private static final String HTTP_ONLY_ATTR = "HttpOnly";

  public static void sendRequestToReact(
      URI uri,
      int reactPort,
      Optional<MediaType> mediaType,
      HttpServletRequest request,
      HttpServletResponse response)
      throws IOException {
    try {
      URIRewriter uriRewriter = new URIRewriter(uri, reactPort);

      callAndProcessReactResponse(
          uri,
          mediaType,
          uriRewriter,
          request,
          response,
          buildReactWebClient(uri, reactPort, uriRewriter, request));
    } catch (URISyntaxException e) {
      throw new IOException(e);
    }
  }

  // Translate the HttpServletRequest to a WebClient request, and URLs to React server URLs.
  private static WebClient buildReactWebClient(
      URI uri, int reactPort, URIRewriter uriRewriter, HttpServletRequest request)
      throws URISyntaxException {
    Builder reactClient = WebClient.create().mutate();

    // Rewrite the URI to point to the React server.
    URI reactUri = URIBuilder.fromUri(uri).setHost("localhost").setPort(reactPort).build();
    reactClient.baseUrl(reactUri.toString());

    // Reformat cookies for the WebClient.
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        reactClient.defaultCookie(cookie.getName(), uriRewriter.rewriteForReact(cookie.getValue()));
      }
    }

    // Reformat headers for the WebClient.
    if (request.getHeaderNames() != null) {
      for (String name : Lists.newArrayList(request.getHeaderNames().asIterator())) {
        if (request.getHeaders(name) != null) {
          for (String value : Lists.newArrayList(request.getHeaders(name).asIterator())) {
            reactClient.defaultHeader(name, uriRewriter.rewriteForReact(value));
          }
        }
      }
    }

    return reactClient.build();
  }

  // Call the React server and translate its response into the HttpServletResponse, and URLs to
  // originalHostPort.
  private static void callAndProcessReactResponse(
      URI uri,
      Optional<MediaType> mediaType,
      URIRewriter uriRewriter,
      HttpServletRequest request,
      HttpServletResponse response,
      WebClient reactClient)
      throws IOException {
    try {
      // Send the request to React and get the response's headers.
      ResponseSpec responseSpec =
          reactClient
              .method(HttpMethod.valueOf(request.getMethod().toUpperCase(Locale.US)))
              .retrieve();
      ResponseEntity<?> reactResponse =
          Objects.requireNonNull(responseSpec.toBodilessEntity().block());

      // Return if React sent an error.
      if (reactResponse.getStatusCode().isError()) {
        response.sendError(reactResponse.getStatusCode().value(), uri.getPath());
        return;
      }

      // Convert the headers and then send them back.
      for (Entry<String, List<String>> header : reactResponse.getHeaders().entrySet()) {
        String name = header.getKey();
        if (HEADERS_THAT_WILL_BE_AUTO_POPULATED.contains(name)) {
          continue;
        } else if (COOKIE_HEADERS.contains(name)) {
          for (String cookieString : header.getValue()) {
            response.addCookie(parseCookieString(uriRewriter.rewriteForSpring(cookieString)));
          }
        } else {
          header
              .getValue()
              .forEach(value -> response.addHeader(name, uriRewriter.rewriteForSpring(value)));
        }
      }
      response.setStatus(reactResponse.getStatusCode().value());
      response.flushBuffer();

      // Stream the response body because the buffer in ResponseEntity is limited.
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

      // If the response is in text format, we need to replace reactHostPort with
      // originalHostPort.
      if (mediaType.isPresent() && mediaType.get().type().equals(MediaType.ANY_TEXT_TYPE.type())) {
        byte[] decodedBytes = decodeBody(reactResponse, reactBody);
        String body = new String(decodedBytes, mediaType.get().charset().or(Charsets.UTF_8));
        reactBody =
            uriRewriter
                .rewriteForSpring(body)
                .getBytes(mediaType.get().charset().or(Charsets.UTF_8));
        response.getOutputStream().write(encodeBody(reactResponse, decodedBytes));
      } else {
        response.getOutputStream().write(reactBody);
      }

      response.getOutputStream().flush();
      response.getOutputStream().close();
    } catch (WrappedIOException e) {
      throw e.getCause();
    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("restricted header name")) {
        throw new IOException(
            "Restricted header names need to be added to jdk.httpclient.allowRestrictedHeaders in"
                + " resources/application.properties.",
            e);
      }
      throw e;
    }
  }

  // Http responses can be encoded (e.g., compressed). So an html page may come back as a series
  // of compressed bytes. This checks the encoding used and unapplies it to the byte array.
  private static byte[] decodeBody(ResponseEntity<?> responseEntity, byte[] body)
      throws IOException {
    // Get the encoding or return the unmodified byte array.
    List<String> encoding = responseEntity.getHeaders().get(HttpHeaders.CONTENT_ENCODING);
    if (encoding == null || encoding.isEmpty()) {
      return body;
    }

    // We only know how to decode a single encoding.
    if (encoding.size() != 1) {
      throw new IOException("Unable to decode bytes: " + encoding);
    }

    // Decode the body using the given encoding.
    switch (encoding.get(0)) {
      case "gzip":
        return ByteStreams.toByteArray(new GZIPInputStream(new ByteArrayInputStream(body)));
      default:
        throw new IOException("Encoding not supported: " + encoding.get(0));
    }
  }

  // Http responses can be encoded (e.g., compressed). This applies the encoding to the byte array
  // to send in the response.
  private static byte[] encodeBody(ResponseEntity<?> responseEntity, byte[] decodedBody)
      throws IOException {
    // Get the encoding or return the unmodified byte array.
    List<String> encoding = responseEntity.getHeaders().get(HttpHeaders.CONTENT_ENCODING);
    if (encoding == null || encoding.isEmpty()) {
      return decodedBody;
    }

    // We only know how to decode a single encoding.
    if (encoding.size() != 1) {
      throw new IOException("Unable to encode bytes: " + encoding);
    }

    // Encode the body using the given encoding.
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    switch (encoding.get(0)) {
      case "gzip":
        try (OutputStream out = new GZIPOutputStream(byteOutputStream)) {
          out.write(decodedBody);
        }
        break;
      default:
        throw new IOException("Encoding not supported: " + encoding.get(0));
    }
    return byteOutputStream.toByteArray();
  }

  private static Cookie parseCookieString(String cookieString) throws IOException {
    String[] params = cookieString.split(";");

    // Extract name/value from first parameter.
    String[] nameValue = params[0].split("=", 2);
    if (nameValue.length != 2) {
      throw new IOException("Cookie string name=value pair error: " + cookieString);
    }
    String name = nameValue[0].trim();
    String value = nameValue[1].trim();
    Cookie cookie = new Cookie(name, value);

    // Extract attributes from the remaining parameters.
    for (int i = 1; i < params.length; ++i) {
      String[] attrNameValue = params[i].split("=", 2);
      if (attrNameValue.length < 1) {
        throw new IOException("Cookie string attribute " + i + " name error: " + cookieString);
      }
      String attrName = attrNameValue[0].trim();
      switch (attrName) {
        case HTTP_ONLY_ATTR:
          cookie.setHttpOnly(true);
          break;
        case SECURE_ATTR:
          cookie.setSecure(true);
          break;
        default:
          if (attrNameValue.length != 2) {
            throw new IOException("Cookie string attribute " + i + " value error: " + cookieString);
          }
          String attrValue = attrNameValue[1].trim();
          cookie.setAttribute(attrName, attrValue);
      }
    }

    return cookie;
  }
}
