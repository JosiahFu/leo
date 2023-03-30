package org.davincischools.leo.server.daos;

import static com.google.common.truth.Truth.assertThat;
import static org.davincischools.leo.database.post_environment_processors.ConfigureTestDatabaseEnvironmentPostProcessor.USE_TEST_DATABASE;

import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {USE_TEST_DATABASE})
public class DatabaseTest {
  @Autowired private Database db;

  @Test
  public void databaseFunctionalTest() {
    assertThat(db).isNotNull();
    assertThat(db.getDistrictRepository()).isNotNull();
  }
}
