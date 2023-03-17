package org.davincischools.leo.database.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin.DatabaseManagement;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestDatabaseInitializer {

  private static final Logger logger = LogManager.getLogger();

  private static final String MYSQL_ROOT_USER = "root";
  private static MySQLContainer<?> mySqlContainer = null;

  public static synchronized MySQLContainer<?> initializeTestDatabase(
      Environment environment, Optional<ConfigurableEnvironment> updateEnvironment)
      throws SQLException, IOException {
    if (mySqlContainer != null) {
      return mySqlContainer;
    }

    // Use information from the existing database properties if they are set.
    URI jdbcUri = URI.create("jdbc:mysql//localhost:3307/test");
    try {
      jdbcUri =
          URI.create(Objects.requireNonNull(environment.getProperty("spring.datasource.url")));
    } catch (NullPointerException | IllegalArgumentException e) {
      // Do nothing. Use the default above.
    }

    String database = Optional.ofNullable(jdbcUri.getPath()).orElse("test");
    String username = "mysql"; // environment.getProperty("spring.datasource.username", "test");
    String password = environment.getProperty("spring.datasource.password", "test");

    // Create a test database if one doesn't exist.
    mySqlContainer =
        new MySQLContainer<>(DockerImageName.parse("mysql").withTag("8-debian"))
            .withDatabaseName(database)
            .withUsername(username)
            .withPassword(password)
            .withEnv("MYSQL_ROOT_PASSWORD", password);
    mySqlContainer.setPortBindings(ImmutableList.of("3307:3306"));
    // Workaround for
    // "STDERR: mysqld: Can't read dir of '/etc/mysql/conf.d/' (Errcode: 13 - Permission denied)"
    // It causes a fatal error and tests hang waiting for the container to start.
    // See: https://github.com/testcontainers/testcontainers-java/issues/914#issuecomment-876965013
    mySqlContainer.addParameter("TC_MY_CNF", null);

    try {
      logger.atWarn().log("Waiting for Docker to start the test database.");
      mySqlContainer.start();
      logger.atInfo().log("Done waiting for Docker to start the test database.");
    } catch (java.lang.IllegalStateException e) {
      // In Windows WSL, it's necessary to start the docker daemon each time you
      // first start WSL. Make a note of that in the exception.
      if (e.getMessage().contains("Could not find a valid Docker environment")) {
        if (new File("/usr/bin/docker").exists()) {
          throw new IllegalArgumentException(
              "If you are using WSL, you may need to start Docker with \"sudo service docker"
                  + " start\".",
              e);
        } else {
          throw new IllegalArgumentException(
              "You may need to install Docker. See instructions at: "
                  + "https://github.com/DaVinciSchools/leo/edit/main/BUILDING.md#build-dependencies.",
              e);
        }
      }
      throw e;
    }

    // Grant permissions to the username.
    DataSource testDb =
        DataSourceBuilder.create()
            .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
            .url(mySqlContainer.getJdbcUrl())
            .username(MYSQL_ROOT_USER)
            .password(mySqlContainer.getPassword())
            .type(MysqlDataSource.class)
            .build();
    try (Connection connection = testDb.getConnection()) {
      PreparedStatement statement =
          connection.prepareStatement(
              // We can't place the database name in SQL using a prepared
              // statement since the database name isn't a string.
              String.format("GRANT ALL ON %s.* TO ?@'%%';", database));
      statement.setString(1, username);
      statement.executeUpdate();
    }

    // Load the schema.
    DatabaseManagement.loadSchema(testDb);

    // Set the Project Leo properties if requested.
    if (updateEnvironment.isPresent()) {
      updateEnvironment
          .get()
          .getPropertySources()
          .addFirst(
              new MapPropertySource(
                  TestDatabaseInitializer.class.getName(),
                  ImmutableMap.<String, Object>builder()
                      .put("spring.datasource.url", mySqlContainer.getJdbcUrl())
                      .put("spring.datasource.username", mySqlContainer.getUsername())
                      .put("spring.datasource.password", mySqlContainer.getPassword())
                      .put(
                          "spring.datasource.driver-class-name",
                          mySqlContainer.getDriverClassName())
                      .build()));
    }

    return mySqlContainer;
  }
}
