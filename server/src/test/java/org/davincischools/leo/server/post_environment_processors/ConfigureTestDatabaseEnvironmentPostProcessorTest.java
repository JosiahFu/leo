package org.davincischools.leo.server.post_environment_processors;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ConfigureTestDatabaseEnvironmentPostProcessorTest {
  @Autowired DataSource springDataSource;

  @Test
  public void springConnectsToDatabaseTest() throws SQLException, IOException {
    try (Connection db = springDataSource.getConnection()) {
      List<String> tableNames = new ArrayList<>();
      ResultSet resultSet = db.getMetaData().getTables(null, null, "%", new String[] {"TABLE"});
      while (resultSet.next()) {
        tableNames.add(resultSet.getString("TABLE_NAME"));
      }
      assertThat(tableNames).contains("user");
    }
  }
}
