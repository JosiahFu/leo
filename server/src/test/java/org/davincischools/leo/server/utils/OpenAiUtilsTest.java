package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import java.net.URI;
import org.davincischools.leo.protos.open_ai.CreateCompletionMessage;
import org.davincischools.leo.protos.open_ai.CreateCompletionRequest;
import org.davincischools.leo.protos.open_ai.CreateCompletionResponse;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests;

@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OpenAiUtilsTest {

  @Autowired ApplicationContext context;

  @Autowired OpenAiUtils openAiUtils;

  @Autowired Environment environment;

  @Test
  public void requestSucceedsTest() throws Exception {
    // If there is no OpenAi key, then skip the test.
    if (openAiUtils.getOpenAiKey().isEmpty()) {
      return;
    }

    try {
      CreateCompletionResponse response =
          openAiUtils
              .sendOpenAiRequest(
                  URI.create(environment.getProperty(OpenAiUtils.OPENAI_API_URL_PROP_NAME)),
                  CreateCompletionRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          CreateCompletionMessage.newBuilder()
                              .setRole("user")
                              .setContent("Say exactly \"Test.\"."))
                      .build(),
                  CreateCompletionResponse.newBuilder())
              .build();
      assertThat(response.getChoicesList()).hasSize(1);
      assertThat(response.getChoices(0).getMessage().getContent()).contains("Test.");
    } catch (TooManyRequests e) {
      // This is a valid response and means our request succeeded.
    }
  }
}
