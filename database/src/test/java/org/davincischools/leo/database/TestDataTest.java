package org.davincischools.leo.database;

import static com.google.common.truth.Truth.assertThat;
import static org.davincischools.leo.database.post_environment_processors.ConfigureTestDatabaseEnvironmentPostProcessor.USE_TEST_DATABASE;

import org.davincischools.leo.database.daos.Database;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.test.TestApplication;
import org.davincischools.leo.database.test.TestData;
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
public class TestDataTest {

  @Autowired TestDatabase testDb;
  @Autowired TestData testData;
  @Autowired Database db;

  @Test
  public void usersAddedTest() {
    User student = db.users.findByEmailAddress(testData.spongeBob.getEmailAddress());
    assertThat(student).isNotNull();
    assertThat(student.getId()).isGreaterThan(0);
  }
}