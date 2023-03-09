package org.davincischools.leo.server.utils;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.extensions.proto.ProtoTruth.assertThat;

import java.io.IOException;
import org.davincischools.leo.protos.open_ai.CreateCompletionMessage;
import org.davincischools.leo.protos.open_ai.CreateCompletionRequest;
import org.davincischools.leo.protos.open_ai.CreateCompletionResponse;
import org.davincischools.leo.server.ServerApplication;
import org.junit.Before;
import org.junit.Test;

public class OpenAiUtilsTest {

  @Before
  public void setup() throws IOException {
    ServerApplication.initializeSystem(new String[] {});
  }

  @Test
  public void requestSucceedsTest() throws Exception {
    CreateCompletionResponse response =
        OpenAiUtils.sendOpenAiRequest(
                OpenAiUtils.COMPLETIONS_URI,
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
  }
}
