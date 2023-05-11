package org.davincischools.leo.database.post_environment_processors;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;

/** Loads additional properties from ${HOME}/project_leo.properties, if present. */
public class LoadCustomProjectLeoProperties implements EnvironmentPostProcessor {

  private static final Logger logger = LogManager.getLogger();

  public static void loadCustomProjectLeoProperties(ConfigurableEnvironment environment) {
    File projectLeoPropertiesFile =
        new File(Optional.ofNullable(System.getenv("HOME")).orElse(""), "project_leo.properties");
    try {
      // Load the custom Project Leo properties file, if it exists.
      if (projectLeoPropertiesFile.exists()
          && !environment
              .getPropertySources()
              .contains(LoadCustomProjectLeoProperties.class.getName())) {
        environment
            .getPropertySources()
            .addFirst(
                new ResourcePropertySource(
                    LoadCustomProjectLeoProperties.class.getName(),
                    projectLeoPropertiesFile.toURI().toString()));
      }

      logger.atInfo().log("Loaded custom Project Leo properties: {}", projectLeoPropertiesFile);
    } catch (IOException e) {
      throw new RuntimeException(
          "Failed to load custom Project Leo properties: " + projectLeoPropertiesFile + ".", e);
    }
  }

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    loadCustomProjectLeoProperties(environment);
  }
}
