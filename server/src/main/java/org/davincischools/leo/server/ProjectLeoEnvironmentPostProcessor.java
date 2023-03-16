package org.davincischools.leo.server;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

/** Loads additional properties from ${HOME}/project_leo.properties, if present. */
public class ProjectLeoEnvironmentPostProcessor implements EnvironmentPostProcessor {

  private static final String ALLOW_RESTRICTED_HEADERS_PROPERTY_KEY =
      "jdk.httpclient.allowRestrictedHeaders";

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    try {
      // This property needs to be copied to System properties.
      // I'm not sure why because this wasn't necessary in the past.
      if (System.getProperty(ALLOW_RESTRICTED_HEADERS_PROPERTY_KEY) == null) {
        Optional.ofNullable(environment.getProperty(ALLOW_RESTRICTED_HEADERS_PROPERTY_KEY))
            .ifPresent(value -> System.setProperty(ALLOW_RESTRICTED_HEADERS_PROPERTY_KEY, value));
      }

      // Load the additional Project Leo properties file, if it exists.
      File projectLeoProperties =
          new File(Optional.ofNullable(System.getenv("HOME")).orElse(""), "project_leo.properties");
      if (projectLeoProperties.exists()) {
        environment
            .getPropertySources()
            .addFirst(
                new ResourcePropertySource(
                    ProjectLeoEnvironmentPostProcessor.class.getName(),
                    projectLeoProperties.toURI().toString()));
      }
    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to load additional properties from \"${HOME}/project_leo.properties\".", e);
    }
  }
}
