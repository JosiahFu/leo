package org.davincischools.leo.server.post_environment_processors;

import java.util.Optional;
import org.davincischools.leo.database.test.TestDatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Starts a test database if project_leo.use.test.database=="true". And sets the spring.datasource.*
 * properties to point to it.
 */
public class ConfigureTestDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {
    try {
      // Check that a test database should be started.
      if (!"true".equals(environment.getProperty("project_leo.use.test.database"))) {
        return;
      }

      // Spin up a test database.
      TestDatabaseInitializer.initializeTestDatabase(environment, Optional.of(environment));
    } catch (Exception e) {
      throw new RuntimeException("Failed to spin up test database.", e);
    }
  }
}
