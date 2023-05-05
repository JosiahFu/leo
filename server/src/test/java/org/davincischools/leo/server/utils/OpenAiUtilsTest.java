package org.davincischools.leo.server.utils;

import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OpenAiUtilsTest {

  @Autowired OpenAiUtils openAiUtils;

  @Test
  public void requestSucceedsTest() {
    // TODO: Disabled until the temporary DB is working again.
    // It fails with a JPA error.
  }
}
