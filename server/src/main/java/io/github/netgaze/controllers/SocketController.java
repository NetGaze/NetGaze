package io.github.netgaze.controllers;

import io.github.netgaze.Agent;
import io.github.netgaze.service.AgentService;
import io.github.netgaze.util.ObjectMapperProvider;
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
    public void registerOrUpdateAgent(String agentJson) throws Exception {
        log.info("Received: {}", agentJson);
        Agent agent = ObjectMapperProvider.getObjectMapper().readValue(agentJson, Agent.class);
        agentService.addOrUpdateAgent(agent);
    }
}