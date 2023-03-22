package org.davincischools.leo.database.test;

import static org.davincischools.leo.database.post_environment_processors.ConfigureTestDatabaseEnvironmentPostProcessor.USE_TEST_DATABASE_KEY;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.database.admin.DatabaseManagement;
import org.davincischools.leo.database.daos.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;
import org.springframework.test.util.TestSocketUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@Component
public class TestDatabase {

  private static final Logger log = LogManager.getLogger();

  public static final String TEST_DATABASE_PORT_KEY = "project_leo.test.database.port";

  public static final String ROOT_USERNAME = "root";
  public static final String ROOT_PASSWORD = "password";
  public static final String ADMIN_USERNAME = "project_leo_admin";
  public static final String ADMIN_PASSWORD = "password";

  private static Map<String, MySQLContainer<?>> mySqlContainers = new HashMap<>();
  private static String lastDataSource = null;

  public TestDatabase(@Autowired ConfigurableEnvironment environment)
      throws SQLException, IOException {
    URI jdbcUri =
        URI.create(
            environment.getProperty("spring.datasource.url", "jdbc:mysql//localhost:3307/test"));
    URI databasePath = URI.create(jdbcUri.getSchemeSpecificPart());
    lastDataSource = Optional.ofNullable(databasePath.getPath()).orElse("/test").substring(1);
    log.atInfo().log("Creating a test database: " + lastDataSource);

    try {
      // Create a test database if one doesn't exist.
      MySQLContainer<?> container;
      synchronized (mySqlContainers) {
        container = mySqlContainers.get(lastDataSource);
        if (container == null) {
          int port =
              environment.getProperty(
                  TEST_DATABASE_PORT_KEY, Integer.class, TestSocketUtils.findAvailableTcpPort());
          container = createContainer(lastDataSource, port);
          mySqlContainers.put(lastDataSource, container);
          DataSource source = getDataSource(container);
          grantAllAccess(source, lastDataSource, ADMIN_USERNAME);
          DatabaseManagement.loadSchema(source);
        }
      }

      String username = environment.getProperty("spring.datasource.username", "test");
      String password = environment.getProperty("spring.datasource.password", "test");
      DataSource source = getDataSource(container);
      createUser(source, username, password);
      grantAllAccess(source, lastDataSource, username);

      // Set database properties, if requested.
      environment
          .getPropertySources()
          .addFirst(
              new MapPropertySource(
                  TestDatabase.class.getName(),
                  ImmutableMap.<String, Object>builder()
                      .put("spring.datasource.url", container.getJdbcUrl())
                      .put("spring.datasource.username", username)
                      .put("spring.datasource.password", password)
                      .put("spring.datasource.driver-class-name", container.getDriverClassName())
                      .put(USE_TEST_DATABASE_KEY, true)
                      .put(Database.DATABASE_SALT_KEY, "salt")
                      .build()));
      System.setProperty(Database.DATABASE_SALT_KEY, "salt");
    } catch (SQLException | IOException | RuntimeException e) {
      environment.getPropertySources().remove(TestDatabase.class.getName());
      throw e;
    }
  }

  public DataSource getDataSource() {
    return getDataSource(
        Objects.requireNonNull(
            mySqlContainers.get(lastDataSource),
            () -> "Data source name not available: " + lastDataSource));
  }

  private static DataSource getDataSource(MySQLContainer<?> container) {
    return DataSourceBuilder.create()
        .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
        .url(container.getJdbcUrl())
        .username(ROOT_USERNAME)
        .password(ROOT_PASSWORD)
        .type(MysqlDataSource.class)
        .build();
  }

  private static MySQLContainer<?> createContainer(String database, int port) {
    MySQLContainer<?> container =
        new MySQLContainer<>(DockerImageName.parse("mysql").withTag("8-debian"))
            .withDatabaseName(database)
            .withUsername(ADMIN_USERNAME)
            .withPassword(ADMIN_PASSWORD)
            .withEnv("MYSQL_ROOT_PASSWORD", ROOT_PASSWORD);
    container.setPortBindings(ImmutableList.of(port + ":3306"));
    // Workaround for
    // "STDERR: mysqld: Can't read dir of '/etc/mysql/conf.d/' (Errcode: 13 - Permission denied)"
    // It causes a fatal error and tests hang waiting for the container to start.
    // See:
    // https://github.com/testcontainers/testcontainers-java/issues/914#issuecomment-876965013
    container.addParameter("TC_MY_CNF", null);

    try {
      container.start();
    } catch (IllegalStateException e) {
      // Ths failure is probably due to Docker Desktop not being installed.
      if (e.getMessage().contains("Could not find a valid Docker environment")) {
        throw new IllegalArgumentException(
            "You may need to install Docker Desktop. See instructions at: "
                + "https://github.com/DaVinciSchools/leo/edit/main/BUILDING.md#build-dependencies.",
            e);
      }
      throw e;
    }

    return container;
  }

  private static void grantAllAccess(DataSource source, String database, String username)
      throws SQLException {
    try (Connection connection = source.getConnection()) {
      // Grant the user permissions.
      PreparedStatement statement =
          connection.prepareStatement(
              // We can't place the database name in SQL using a prepared
              // statement since the database name isn't a string.
              String.format("GRANT ALL ON %s.* TO ?@'%%';", database));
      statement.setString(1, username);
      statement.executeUpdate();
    }
  }

  private static void createUser(DataSource source, String username, String password)
      throws SQLException {
    try (Connection connection = source.getConnection()) {
      // Drop an existing user.
      PreparedStatement statement = connection.prepareStatement("DROP USER ?@'%';");
      statement.setString(1, username);
      try {
        statement.executeUpdate();
      } catch (SQLException e) {
        // It's okay if the user doesn't exist and this fails.
      }

      // Create the user.
      statement = connection.prepareStatement("CREATE USER ?@'%' IDENTIFIED BY ?;");
      statement.setString(1, username);
      statement.setString(2, password);
      statement.executeUpdate();
    }
  }
}
