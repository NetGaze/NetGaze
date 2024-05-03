package io.github.amithkoujalgi.netwatch.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amithkoujalgi.netwatch.Ack;
import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.eventlistener.AgentRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SocketController {

  @MessageMapping("/event-listener")
  @SendTo("/topic/ack")
  public Ack registerOrUpdateAgent(Agent agent) throws Exception {
    AgentRegister.getInstance().updateAgent(agent);
    log.info("Received: " + new ObjectMapper().writeValueAsString(agent));
    return new Ack("ok");
  }
}