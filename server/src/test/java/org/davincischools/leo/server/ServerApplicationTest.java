package org.davincischools.leo.server;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static org.davincischools.leo.database.post_environment_processors.ConfigureTestDatabaseEnvironmentPostProcessor.USE_TEST_DATABASE;
import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import java.util.Optional;
import org.davincischools.leo.database.daos.User;
import org.davincischools.leo.database.test.TestData;
import org.davincischools.leo.database.utils.Database;
import org.davincischools.leo.protos.message_of_the_day.MessageRequest;
import org.davincischools.leo.protos.message_of_the_day.MessageResponse;
import org.davincischools.leo.server.controllers.ReactResourceController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(properties = {USE_TEST_DATABASE})
public class ServerApplicationTest {
  @Autowired private ReactResourceController controller;
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private TestData testData;
  @Autowired private Database db;

  @Value(value = "${" + LOCAL_SERVER_PORT_PROPERTY + "}")
  private int port;

  @Before
  public void setUp() {
    // The configuration in ServerApplication.ServerApplicationConfigurer is
    // not applied in the test instance. So, we need to apply it manually.
    //
    // I tried referring to the class using the test class annotations. But,
    // that didn't seem to work. But, that would be the preferred solution.
    ServerApplication.ServerApplicationConfigurer configurer =
        new ServerApplication.ServerApplicationConfigurer();
    configurer.extendMessageConverters(restTemplate.getRestTemplate().getMessageConverters());
  }

  @Test
  public void controllerLoadsTest() {
    assertThat(controller).isNotNull();
  }

  @Test
  public void indexPageLoadsTest() {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/", String.class))
        .contains("Leo");
  }

  @Test
  public void messageOfTheDayTest() throws Exception {
    ResponseEntity<MessageResponse> response =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/api/protos/MessageOfTheDayService/GetMessage",
            MessageRequest.newBuilder().setId(0).build(),
            MessageResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getMessage()).contains("Ikigai");
  }

  @Test
  public void messageOfTheDayEmptyRequestBodyTest() throws Exception {
    ResponseEntity<MessageResponse> response =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/api/protos/MessageOfTheDayService/GetMessage",
            // Serializing a default instance will result in no bytes.
            MessageRequest.getDefaultInstance(),
            MessageResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getMessage()).isNotEmpty();
  }

  @Test
  public void messageOfTheDayEmptyResponseBodyTest() throws Exception {
    ResponseEntity<MessageResponse> response =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/api/protos/MessageOfTheDayService/GetMessage",
            MessageRequest.newBuilder().setId(-1).build(),
            MessageResponse.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    // Serializing the default instance returned from MOTDService will result in no bytes.
    assertThat(response.getBody()).isNull();
  }

  @Test
  public void usersAddedTest() {
    Optional<User> student =
        db.getUserRepository().findByEmailAddress(testData.student.getEmailAddress());
    assertThat(student).isPresent();
    assertThat(student.get().getId()).isGreaterThan(0);
  }
}
