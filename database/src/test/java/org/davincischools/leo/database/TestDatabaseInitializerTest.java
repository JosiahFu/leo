package org.davincischools.leo.database;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.davincischools.leo.database.test.TestDatabaseInitializer;
import org.junit.Test;
import org.springframework.mock.env.MockEnvironment;

public class TestDatabaseInitializerTest {

  @Test
  public void testDatabaseInitializesTest() throws SQLException, IOException {
    MockEnvironment environment = new MockEnvironment();
    environment.setProperty("project_leo.use.test.database", "true");

    DataSource source =
        TestDatabaseInitializer.initializeTestDatabase(environment, Optional.of(environment));

    try (Connection connection = source.getConnection()) {
      List<String> tableNames = new ArrayList<>();
      ResultSet resultSet =
          connection.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
      while (resultSet.next()) {
        tableNames.add(resultSet.getString("TABLE_NAME"));
      }
      assertThat(tableNames).contains("users");
    }
  }
}
