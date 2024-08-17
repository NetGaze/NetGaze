package io.github.netgaze.service.impl;

import io.github.netgaze.controllers.CAgent;
import io.github.netgaze.service.AgentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentsServiceImpl implements AgentService {

    @Override
    public void addOrUpdateAgent(CAgent agent) {

    }

    @Override
    public List<CAgent> listAgents() {
        return List.of();
    }
}

