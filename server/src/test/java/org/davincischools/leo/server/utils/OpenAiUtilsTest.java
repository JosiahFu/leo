package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import java.util.Optional;
import org.davincischools.leo.protos.open_ai.OpenAiMessage;
import org.davincischools.leo.protos.open_ai.OpenAiRequest;
import org.davincischools.leo.protos.open_ai.OpenAiResponse;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.reactive.function.client.WebClientResponseException.TooManyRequests;

@SpringBootTest(classes = ServerApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class OpenAiUtilsTest {

  @Autowired OpenAiUtils openAiUtils;

  @Test
  public void requestSucceedsTest() throws Exception {
    // If there is no OpenAi key, then skip the test.
    if (openAiUtils.getOpenAiKey().isEmpty()) {
      return;
    }

    try {
      OpenAiResponse response =
          openAiUtils
              .sendOpenAiRequest(
                  OpenAiRequest.newBuilder()
                      .setModel(OpenAiUtils.GPT_3_5_TURBO_MODEL)
                      .addMessages(
                          OpenAiMessage.newBuilder()
                              .setRole("user")
                              .setContent("Say exactly \"Test.\"."))
                      .build(),
                  OpenAiResponse.newBuilder(),
                  Optional.empty())
              .build();
      assertThat(response.getChoicesList()).hasSize(1);
      assertThat(response.getChoices(0).getMessage().getContent()).contains("Test.");
    } catch (TooManyRequests e) {
      // This is a valid response and means our request succeeded.
    }
  }
}
