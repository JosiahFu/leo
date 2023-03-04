package org.davincischools.leo.server.controllers;

import com.google.common.collect.ImmutableList;
import java.util.Optional;
import java.util.Random;
import org.davincischools.leo.protos.message_of_the_day.MessageRequest;
import org.davincischools.leo.protos.message_of_the_day.MessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/** Handles requests for the MessageOfTheDay service in message_of_the_day.proto. */
@Controller
public class MessageOfTheDayController {

  // Eventually, this would be stored in a database.
  ImmutableList<String> messages =
      ImmutableList.of(
          "Let's hear it for Ikigai!",
          "What are you good at? Let's figure out!",
          "What do you love? Let's figure out!",
          "What does the world need? Let's figure out!",
          "What can you be paid for? Let's figure out!");

  Random rand = new Random();

  // If the client sends an empty MessageRequest then the body of the
  // request will have no bytes. Spring will then send either an optional
  // value that's empty, or a null value if the parameter is @Nullable.
  // So, handlers need to accept this type of input.
  @PostMapping(value = "/api/protos/MessageOfTheDayService/GetMessage")
  @ResponseBody
  public MessageResponse getResource(@RequestBody Optional<MessageRequest> request) {
    String message;
    if (request.isPresent() && request.get().hasId()) {
      // -1 is just used for tests that want an empty response.
      if (request.get().getId() == -1) {
        // Likewise, if this returns an empty MessageResponse, then the body of
        // the response will be null. So, the handler of the response needs to
        // be ready for that as well.
        return MessageResponse.getDefaultInstance();
      }
      message = messages.get(request.get().getId());
    } else {
      message = messages.get(rand.nextInt(messages.size()));
    }
    return MessageResponse.newBuilder().setMessage(message).build();
  }
}
