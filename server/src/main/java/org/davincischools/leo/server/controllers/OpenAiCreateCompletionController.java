package org.davincischools.leo.server.controllers;

import java.io.IOException;
import java.util.Optional;
import org.davincischools.leo.protos.open_ai.CreateCompletionRequest;
import org.davincischools.leo.protos.open_ai.CreateCompletionResponse;
import org.davincischools.leo.server.utils.OpenAiUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/** OpenAI's Create Completion API. */
@Controller
public class OpenAiCreateCompletionController {

  // If the client sends an empty MessageRequest then the body of the
  // request will have no bytes. Spring will then send either an optional
  // value that's empty, or a null value if the parameter is @Nullable.
  // So, handlers need to accept this type of input.
  @PostMapping(value = "/api/protos/OpenAiService/CreateCompletion")
  @ResponseBody
  public CreateCompletionResponse createCompletion(
      @RequestBody Optional<CreateCompletionRequest> request) throws IOException {
    request = Optional.of(request.orElse(CreateCompletionRequest.getDefaultInstance()));
    CreateCompletionResponse.Builder response =
        OpenAiUtils.sendOpenAiRequest(
            OpenAiUtils.COMPLETION_URI, request.get(), CreateCompletionResponse.newBuilder());
    return response.build();
  }
}
