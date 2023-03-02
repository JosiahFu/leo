package org.davincischools.leo.server.controllers;

import com.google.common.collect.ImmutableList;
import java.util.Random;
import org.davincischools.leo.protos.message_of_the_day.MessageRequest;
import org.davincischools.leo.protos.message_of_the_day.MessageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/** Handles requests for the MessageOfTheDay service in message_of_the_day.proto. */
@Controller
public class MessageOfTheDayController {

  // Eventually, this would be stored in a database.
  ImmutableList<String> messages =
      ImmutableList.of(
          "Let's hear it for <i>Ikigai</i>!",
          "What are you good at? Let's figure out!",
          "What do you love? Let's figure out!",
          "What does the world need? Let's figure out!",
          "What can you be paid for? Let's figure out!");

  Random rand = new Random();

  @RequestMapping("/api/protos/MessageOfTheDay/GetMessage")
  public @ResponseBody MessageResponse getResource(@RequestBody MessageRequest request) {
    String message;
    if (request.hasId()) {
      message = messages.get(request.getId());
    } else {
      message = messages.get(rand.nextInt(messages.size()));
    }
    return MessageResponse.newBuilder().setMessage(message).build();
  }
}
