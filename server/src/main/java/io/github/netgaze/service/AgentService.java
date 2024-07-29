package io.github.netgaze.service;

import io.github.netgaze.Agent;

import java.util.List;

public interface AgentService {
    void addOrUpdateAgent(Agent agent);

    List<Agent> listAgents();
}
