package io.github.netgaze.service;

import io.github.netgaze.controllers.CAgent;

import java.util.List;

public interface AgentService {
    void addOrUpdateAgent(CAgent agent);

    List<CAgent> listAgents();
}
