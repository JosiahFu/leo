package org.davincischools.leo.database.post_environment_processors;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

/** Loads additional properties from ${HOME}/project_leo.properties, if present. */
public class LoadCustomProjectLeoProperties implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    try {
      // Load the additional Project Leo properties file, if it exists.
      File projectLeoProperties =
          new File(Optional.ofNullable(System.getenv("HOME")).orElse(""), "project_leo.properties");
      if (projectLeoProperties.exists()) {
        environment
            .getPropertySources()
            .addFirst(
                new ResourcePropertySource(
                    LoadCustomProjectLeoProperties.class.getName(),
                    projectLeoProperties.toURI().toString()));
      }
    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to load additional properties from \"${HOME}/project_leo.properties\".", e);
    }
  }
}
