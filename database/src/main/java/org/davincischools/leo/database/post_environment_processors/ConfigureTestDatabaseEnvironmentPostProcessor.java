package org.davincischools.leo.database.post_environment_processors;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.test.TestDatabase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

/**
 * Starts a test database if project_leo.use.test.database=="true". And sets the spring.datasource.*
 * properties to point to it.
 */
public class ConfigureTestDatabaseEnvironmentPostProcessor implements EnvironmentPostProcessor {

  public static final String USE_TEST_DATABASE_KEY = "project_leo.use.test.database";
  public static final String USE_TEST_DATABASE = USE_TEST_DATABASE_KEY + "=true";

  private static final Logger log = LogManager.getLogger();

  @Override
  public void postProcessEnvironment(
      ConfigurableEnvironment environment, SpringApplication application) {

    // Check that a test database should be started.
    if (!"true".equals(environment.getProperty(USE_TEST_DATABASE_KEY, "false"))) {
      return;
    }

    log.atDebug().log("Configuring test database environment.");

    try {
      // Set JDBC properties.
      Map<String, Object> properties = new HashMap<>();
      properties.computeIfAbsent(
          "spring.datasource.url",
          key ->
              Optional.ofNullable(environment.getProperty(key))
                  .orElse("jdbc:mysql://localhost:3306/project_leo"));
      properties.computeIfAbsent(
          "spring.datasource.username",
          key -> Optional.ofNullable(environment.getProperty(key)).orElse("test"));
      properties.computeIfAbsent(
          "spring.datasource.password",
          key -> Optional.ofNullable(environment.getProperty(key)).orElse("test"));
      properties.computeIfAbsent(
          "spring.datasource.driver-class-name",
          key ->
              Optional.ofNullable(environment.getProperty(key))
                  .orElse(com.mysql.cj.jdbc.Driver.class.getName()));

      // Set JPA and Hibernate properties.
      properties.computeIfAbsent(
          "spring.jpa.hibernate.ddl-auto",
          key -> Optional.ofNullable(environment.getProperty(key)).orElse("none"));
      properties.computeIfAbsent(
          "spring.jpa.open-in-view",
          key -> Optional.ofNullable(environment.getProperty(key)).orElse("false"));
      properties.computeIfAbsent(
          "hibernate.dialect",
          key ->
              Optional.ofNullable(environment.getProperty(key))
                  .orElse(org.hibernate.dialect.MySQLDialect.class.getName()));

      // Spin up test database.
      environment
          .getPropertySources()
          .addFirst(
              new MapPropertySource(
                  ConfigureTestDatabaseEnvironmentPostProcessor.class.getName(), properties));
      new TestDatabase(environment);
    } catch (Exception e) {
      environment
          .getPropertySources()
          .remove(ConfigureTestDatabaseEnvironmentPostProcessor.class.getName());
      throw new RuntimeException("Failed to spin up test database.", e);
    }
  }
}
