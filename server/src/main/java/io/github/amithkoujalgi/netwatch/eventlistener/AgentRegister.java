package io.github.amithkoujalgi.netwatch.eventlistener;


import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.github.amithkoujalgi.netwatch.models.graph.Graph;
import io.github.amithkoujalgi.netwatch.models.graph.GraphEdge;
import io.github.amithkoujalgi.netwatch.models.graph.GraphEdgeData;
import io.github.amithkoujalgi.netwatch.models.graph.GraphNode;
import io.github.amithkoujalgi.netwatch.models.graph.GraphNodeData;
import io.github.amithkoujalgi.netwatch.models.graph.GraphNodePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Getter;

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
      graphNodes.add(
          new GraphNode(new GraphNodeData(a.getHost(), null), new GraphNodePosition(0, 0)));
      for (Connection c : a.getConnections()) {
        graphNodes.add(
            new GraphNode(new GraphNodeData(c.getHost(), null), new GraphNodePosition(0, 0)));
        graphEdges.add(new GraphEdge(
            new GraphEdgeData(UUID.randomUUID().toString(), a.getHost(), c.getHost())));
      }
    }

    Graph g = new Graph();
    g.setNodes(graphNodes);
    g.setEdges(graphEdges);
    return g;
  }
}
