package io.github.netgaze.controllers;

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
    public void registerOrUpdateAgent(String agentJson) throws Exception {
        log.info("Received: {}", agentJson);
        CAgent agent = ObjectMapperProvider.getObjectMapper().readValue(agentJson, CAgent.class);
        System.out.println("Collected agent info");
//        agentService.addOrUpdateAgent(agent);
    }
}