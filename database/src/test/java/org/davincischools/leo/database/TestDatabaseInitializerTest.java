package org.davincischools.leo.database;

import static com.google.common.truth.Truth.assertThat;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.davincischools.leo.database.test.TestDatabaseInitializer;
import org.junit.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.mock.env.MockEnvironment;
import org.testcontainers.containers.MySQLContainer;

public class TestDatabaseInitializerTest {

  @Test
  public void testDatabaseInitializesTest() throws SQLException, IOException {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("project_leo.use.test.database", "true");

    try (MySQLContainer<?> mySqlContainer =
            TestDatabaseInitializer.initializeTestDatabase(environment, Optional.of(environment));
        Connection db =
            DataSourceBuilder.create()
                .driverClassName(mySqlContainer.getDriverClassName())
                .url(mySqlContainer.getJdbcUrl())
                .username(mySqlContainer.getUsername())
                .password(mySqlContainer.getPassword())
                .type(MysqlDataSource.class)
                .build()
                .getConnection()) {

      List<String> tableNames = new ArrayList<>();
      ResultSet resultSet = db.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
      while (resultSet.next()) {
        tableNames.add(resultSet.getString("TABLE_NAME"));
      }

      assertThat(environment.getProperty("spring.datasource.url"))
          .isEqualTo(mySqlContainer.getJdbcUrl());
      assertThat(tableNames).contains("users");
    }
  }
}
