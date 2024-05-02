package io.github.amithkoujalgi.netwatch.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amithkoujalgi.netwatch.Ack;
import io.github.amithkoujalgi.netwatch.Agent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SocketController {

    @MessageMapping("/event-listener")
    @SendTo("/topic/ack")
    public Ack greeting(Agent message) throws Exception {
        log.info("Received: " + new ObjectMapper().writeValueAsString(message));
        return new Ack("ok");
    }
}