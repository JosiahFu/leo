package org.davincischools.leo.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.davincischools.leo.server.SpringConstants.LOCAL_SERVER_PORT_PROPERTY;

import org.davincischools.leo.server.controllers.ReactResourceController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class ServerApplicationTest {
  @Autowired private ReactResourceController controller;

  @Value(value = "${" + LOCAL_SERVER_PORT_PROPERTY + "}")
  private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Test
  public void controllerLoadsTest() {
    assertThat(controller).isNotNull();
  }

  @Test
  public void indexPageLoadsTest() {
    assertThat(restTemplate.getForObject("http://localhost:" + port + "/", String.class))
        .contains("Leo");
  }
}
