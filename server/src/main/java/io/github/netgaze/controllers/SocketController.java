package io.github.netgaze.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.netgaze.Agent;
import io.github.netgaze.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class SocketController {
    private final AgentService agentService;

    public SocketController(AgentService agentService) {
        this.agentService = agentService;
    }

    @MessageMapping("/event-listener")
    public void registerOrUpdateAgent(Agent agent) throws Exception {
        log.info("Received: {}", new ObjectMapper().writeValueAsString(agent));
        agentService.addOrUpdateAgent(agent);
    }
}