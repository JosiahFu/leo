package org.davincischools.leo.server;

import static com.google.common.truth.Truth.assertThat;
import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ServerApplicationTest {
  @Autowired private ReactResourceController controller;
  @Autowired private TestRestTemplate restTemplate;

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
}
