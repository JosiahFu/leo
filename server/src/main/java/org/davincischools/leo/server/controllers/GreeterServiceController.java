package org.davincischools.leo.server.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.davincischools.leo.proto.greeter.HelloReply;
import org.davincischools.leo.proto.greeter.HelloRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/** Handles requests for the Greeter service in greeter.proto. */
@Controller
public class GreeterServiceController {

  private static final Logger log = LogManager.getLogger();

  @RequestMapping("/api/Greeter")
  public @ResponseBody HelloReply getResource(@RequestBody HelloRequest request) {
    log.atInfo().log("Greeter request: {}", request);
    HelloReply.Builder reply = HelloReply.newBuilder();
    reply.setReplyMessage("Hello " + request.getGreeterName());
    return reply.build();
  }
}
