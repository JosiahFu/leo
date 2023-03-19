package org.davincischools.leo.database.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.davincischools.leo.database.admin.DatabaseManagement;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestDatabaseInitializer {

  public static final String ROOT_USERNAME = "root";
  public static final String ROOT_PASSWORD = "password";
  public static final String ADMIN_USERNAME = "project_leo_admin";
  public static final String ADMIN_PASSWORD = "password";

  private static MySQLContainer<?> mySqlContainer = null;
  private static DataSource mySqlSource = null;

  public static synchronized DataSource initializeTestDatabase(
      Environment environment, Optional<ConfigurableEnvironment> updateEnvironment)
      throws SQLException, IOException {

    URI jdbcUri = URI.create("jdbc:mysql//localhost:3307/test");
    String database = Optional.ofNullable(jdbcUri.getPath()).orElse("test");
    String username = environment.getProperty("spring.datasource.username", "test");
    String password = environment.getProperty("spring.datasource.password", "test");

    // Create a test database if one doesn't exist.
    if (mySqlContainer == null) {
      MySQLContainer<?> container = createContainer(database);
      DataSource source =
          DataSourceBuilder.create()
              .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
              .url(container.getJdbcUrl())
              .username(ROOT_USERNAME)
              .password(ROOT_PASSWORD)
              .type(MysqlDataSource.class)
              .build();
      grantAllAccess(source, database, ADMIN_USERNAME);
      DatabaseManagement.loadSchema(source);

      mySqlContainer = container;
      mySqlSource = source;
    }
    createUser(database, username, password);
    grantAllAccess(mySqlSource, database, username);

    // Set database properties, if requested.
    if (updateEnvironment.isPresent()) {
      updateEnvironment
          .get()
          .getPropertySources()
          .addFirst(
              new MapPropertySource(
                  TestDatabaseInitializer.class.getName(),
                  ImmutableMap.<String, Object>builder()
                      .put("spring.datasource.url", mySqlContainer.getJdbcUrl())
                      .put("spring.datasource.username", username)
                      .put("spring.datasource.password", password)
                      .put(
                          "spring.datasource.driver-class-name",
                          mySqlContainer.getDriverClassName())
                      .build()));
    }

    return DataSourceBuilder.create()
        .driverClassName(org.testcontainers.jdbc.ContainerDatabaseDriver.class.getName())
        .url(mySqlContainer.getJdbcUrl())
        .username(username)
        .password(password)
        .type(MysqlDataSource.class)
        .build();
  }

  private static MySQLContainer<?> createContainer(String database) {
    MySQLContainer<?> container =
        new MySQLContainer<>(DockerImageName.parse("mysql").withTag("8-debian"))
            .withDatabaseName(database)
            .withUsername(ADMIN_USERNAME)
            .withPassword(ADMIN_PASSWORD)
            .withEnv("MYSQL_ROOT_PASSWORD", ROOT_PASSWORD);
    container.setPortBindings(ImmutableList.of("3307:3306"));
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

  private static void createUser(String database, String username, String password)
      throws SQLException {
    try (Connection connection = mySqlSource.getConnection()) {
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
