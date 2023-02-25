package org.davincischools.leo.server.controllers;

import static org.davincischools.leo.server.CommandLineArguments.COMMAND_LINE_ARGUMENTS;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.server.utils.HttpServletProxy;
import org.davincischools.leo.server.utils.URIBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Serves static React content from the java classpath or proxies requests to a development React
 * web server if the <code>--react_port</code> flag is present.
 *
 * <p>In addition to building the React server as part of the Maven configuration, the built files
 * are copied into <code>/target/classes/.../www</code>. This places the built content in the <code>
 * *.jar</code> and makes it available via <code>ClassLoader.getSystemResourceAsStream</code>.
 */
@Controller
public class ReactResourceController {

  private static final Logger log = LogManager.getLogger();

  private static final ImmutableMap<String, MediaType> EXTENSIONS_TO_MIME_TYPES =
      ImmutableMap.<String, MediaType>builder()
          .put("css", MediaType.CSS_UTF_8)
          .put("html", MediaType.HTML_UTF_8)
          .put("ico", MediaType.ICO)
          .put("js", MediaType.JAVASCRIPT_UTF_8)
          .put("json", MediaType.JSON_UTF_8)
          .put("map", MediaType.JSON_UTF_8)
          .put("png", MediaType.PNG)
          .put("svg", MediaType.SVG_UTF_8)
          .put("txt", MediaType.PLAIN_TEXT_UTF_8)
          .build();

  @RequestMapping({
    "/",
    "/favicon.ico",
    "/index.html",
    "/installHooks.js",
    "/logo*.png",
    "/manifest.json",
    "/robots.txt",
    "/static/**"
  })
  public void getResource(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.atInfo().log("Request for: {}", request.getRequestURI());

    URI uri = getUri(request);
    Optional<MediaType> mediaType = getResponseMimeType(uri);
    if (COMMAND_LINE_ARGUMENTS.reactPort != null) {
      // Forward the request to the React server running locally.
      HttpServletProxy.sendRequestToReact(uri, mediaType, request, response);
    } else {
      // Get and copy a resource from the classpath.
      getResponseMimeType(uri).map(Object::toString).ifPresent(response::setContentType);
      try (InputStream in =
          ClassLoader.getSystemResourceAsStream(
              "org/davincischools/leo/server/www" + uri.getPath())) {
        if (in == null) {
          log.atError().log("Resource not found: {}", uri.getPath());
          response.sendError(HttpServletResponse.SC_NOT_FOUND, uri.getPath());
          return;
        }
        ByteStreams.copy(in, response.getOutputStream());
        response.getOutputStream().flush();
        response.getOutputStream().close();
      }
    }
  }

  private static URI getUri(HttpServletRequest request) throws IOException {
    try {
      // Get the normalized request path (e.g., removing "..").
      URI uri = URI.create(request.getRequestURL().toString()).normalize();
      // If a path was not provided, return the index page.
      if (uri.getPath().equals("/")) {
        uri = URIBuilder.fromUri(uri).setPath("/index.html").build();
      }
      return uri;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  private static Optional<MediaType> getResponseMimeType(URI uri) {
    // Get resource type from filename type.
    String fileExtension = Files.getFileExtension(uri.getPath());
    return Optional.ofNullable(EXTENSIONS_TO_MIME_TYPES.get(fileExtension));
  }
}
