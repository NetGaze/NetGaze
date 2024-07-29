package io.github.netgaze.core;


import io.github.netgaze.Agent;
import io.github.netgaze.Connection;
import io.github.netgaze.models.graph.*;
import lombok.Getter;

import java.util.*;
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
        if (agent.getLastSeenAt() == null) {
            agent.setLastSeenAt(new Date());
        }
        if (agent.getConnections() != null) {
            for (Connection c : agent.getConnections()) {
                if (c.getLastCheckedAt() == null) {
                    c.setLastCheckedAt(new Date());
                }
            }
        }
        boolean found = false;
        for (Agent a : agents) {
            if (a.getName().equals(agent.getName())) {
                a.setHost(agent.getHost());
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

    public Graph getGraph() {
        List<GraphNode> graphNodes = new ArrayList<>();
        List<GraphEdge> graphEdges = new ArrayList<>();

        for (Agent a : AgentRegister.getInstance().getAgents()) {
            boolean isAgentActive = true;
            Map<String, Object> agentProps = new HashMap<>();
            agentProps.put("lastSeenAt", a.getLastSeenAt().toString());
            agentProps.put("active", isAgentActive);
            agentProps.put("name", a.getName());
            agentProps.put("host", a.getHost());
            graphNodes.add(
                    new GraphNode(
                            new GraphNodeData(a.getHost(), null),
                            new GraphNodePosition(0, 0),
                            agentProps,
                            (isAgentActive ? "node-active" : "node-inactive") + " agent"
                    )
            );
            if (a.getConnections() != null) {
                for (Connection c : a.getConnections()) {
                    boolean isConnectionActive = c.isActive();
                    Map<String, Object> connProps = new HashMap<>();
                    connProps.put("lastCheckedAt", c.getLastCheckedAt().toString());
                    connProps.put("active", isConnectionActive);
                    connProps.put("name", c.getName());
                    connProps.put("host", c.getHost());
                    connProps.put("port", c.getPort());
                    connProps.put("type", c.getType());
                    graphNodes.add(
                            new GraphNode(
                                    new GraphNodeData(c.getHost(), null),
                                    new GraphNodePosition(0, 0),
                                    connProps,
                                    (isConnectionActive ? "node-active" : "node-inactive") + " connection"
                            )
                    );
                    graphEdges.add(new GraphEdge(
                            new GraphEdgeData(UUID.randomUUID().toString(), a.getHost(), c.getHost())));
                }
            }
        }

        Graph g = new Graph();
        g.setNodes(graphNodes);
        g.setEdges(graphEdges);
        return g;
    }
}
