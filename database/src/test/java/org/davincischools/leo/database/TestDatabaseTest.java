package org.davincischools.leo.database;

import static com.google.common.truth.Truth.assertThat;
import static org.davincischools.leo.database.post_environment_processors.ConfigureTestDatabaseEnvironmentPostProcessor.USE_TEST_DATABASE;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.davincischools.leo.database.test.TestApplication;
import org.davincischools.leo.database.test.TestDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {USE_TEST_DATABASE})
public class TestDatabaseTest {

  @Autowired TestDatabase testDatabase;

  @Test
  public void testDatabaseInitializesTest() throws SQLException, IOException {
    DataSource source = testDatabase.getDataSource();
    try (Connection connection = source.getConnection()) {
      List<String> tableNames = new ArrayList<>();
      ResultSet resultSet =
          connection.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
      while (resultSet.next()) {
        tableNames.add(resultSet.getString("TABLE_NAME"));
      }
      assertThat(tableNames).contains("user_x");
    }
  }
}
