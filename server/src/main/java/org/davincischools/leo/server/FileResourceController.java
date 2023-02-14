package org.davincischools.leo.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.google.common.net.MediaType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class serves the static resources that were built by 'npm run build' in one of the /clients/
 * subfolders, and placed into a /build/ subfolder within the client.
 *
 * <p>As part of the Maven build script, the /build/ subfolder's contents are copied into the
 * /target/classes/.../FileResourceController/www Maven output directory. This makes them available
 * as resources relative to this class's file.
 */
@RestController
public class FileResourceController {
  private static final Logger log = LogManager.getLogger();

  private static final String UNKNOWN_MIME_TYPE = "unknown";
  private static final ImmutableMap<String, String> EXTENSIONS_TO_MIME_TYPES =
      ImmutableMap.<String, String>builder()
          .put("css", MediaType.CSS_UTF_8.toString())
          .put("html", MediaType.HTML_UTF_8.toString())
          .put("ico", MediaType.ICO.toString())
          .put("js", MediaType.JAVASCRIPT_UTF_8.toString())
          .put("json", MediaType.JSON_UTF_8.toString())
          .put("map", MediaType.JSON_UTF_8.toString())
          .put("png", MediaType.PNG.toString())
          .put("svg", MediaType.SVG_UTF_8.toString())
          .put("txt", MediaType.PLAIN_TEXT_UTF_8.toString())
          .build();

  @GetMapping("/**")
  public void getResource(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    log.atInfo().log("Request for: {}", request.getRequestURI());

    // Get the normalized (e.g., removing "..") requested artifact path.
    URI uri = URI.create(request.getRequestURI()).normalize();
    if (uri.getPath().equals("/")) {
      uri = URI.create("/index.html");
    }

    // Get resource type from filename type.
    String fileExtension = Files.getFileExtension(uri.getPath());
    String mimeType = EXTENSIONS_TO_MIME_TYPES.getOrDefault(fileExtension, UNKNOWN_MIME_TYPE);
    if (mimeType.equals(UNKNOWN_MIME_TYPE)) {
      log.atError().log("Unrecognized file extension: {}", fileExtension);
      mimeType = MediaType.PLAIN_TEXT_UTF_8.toString();
    }

    // Get and return resource.
    try (InputStream in = FileResourceController.class.getResourceAsStream("www" + uri.getPath())) {
      if (in == null) {
        log.atError().log("Resource not found: {}", fileExtension);
        response.sendError(HttpServletResponse.SC_NOT_FOUND, uri.getPath());
        return;
      }
      ByteStreams.copy(in, response.getOutputStream());
      response.setContentType(mimeType);
    }
  }
}
