package io.github.netgaze.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.netgaze.Agent;
import io.github.netgaze.core.AgentRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SocketController {

    @MessageMapping("/event-listener")
    public void registerOrUpdateAgent(Agent agent) throws Exception {
        AgentRegister.getInstance().updateAgent(agent);
        log.info("Received: " + new ObjectMapper().writeValueAsString(agent));
    }
}