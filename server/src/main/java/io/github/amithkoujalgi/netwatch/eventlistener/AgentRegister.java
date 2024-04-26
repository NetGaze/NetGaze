package io.github.amithkoujalgi.netwatch.eventlistener;


import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public final class AgentRegister {

    private static AgentRegister INSTANCE;

    private final List<Agent> agents = new CopyOnWriteArrayList<>();

    private AgentRegister() {
    }

    public static AgentRegister getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AgentRegister();
        }
        return INSTANCE;
    }

    public void updateAgent(Agent agent) {
        boolean found = false;
        for (Agent a : agents) {
            if (a.getName().equals(agent.getName())) {
                a.setConnections(agent.getConnections());
                found = true;
            }
        }
        if (!found) {
            agents.add(agent);
        }
    }

    public List<Connection> getConnections(String agent) throws Exception {
        for (Agent a : agents) {
            if (a.getName().equals(agent)) {
                return a.getConnections();
            }
        }
        throw new Exception("Agent not found!");
    }
}
