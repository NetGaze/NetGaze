package io.github.amithkoujalgi.netwatch.controllers;

import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.github.amithkoujalgi.netwatch.ConnectionType;
import io.github.amithkoujalgi.netwatch.eventlistener.AgentRegister;
import io.github.amithkoujalgi.netwatch.models.graph.Graph;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "", description = "Agent APIs")
@RestController
public class AgentController {


  @Operation(summary = "List agents")
  @ApiResponse(responseCode = "200", content = {
      @Content(array = @ArraySchema(schema = @Schema(implementation = List.class, subTypes = {
          Agent.class})), mediaType = "application/json")})
  @GetMapping("/agents")
  public List<Agent> agents() {

    Connection c = new Connection();
    c.setName("C1");
    c.setHost("google.com");
    c.setPort(80);
    c.setType(ConnectionType.HTTP);

    Agent agent = new Agent();
    agent.setName("test");
    agent.setHost("0.1.2.3");
    agent.setConnections(Collections.singletonList(c));

    AgentRegister.getInstance().updateAgent(agent);
    return AgentRegister.getInstance().getAgents();
  }


  @Operation(summary = "Get connections of an agent")
  @ApiResponse(responseCode = "200", content = {
      @Content(array = @ArraySchema(schema = @Schema(implementation = List.class, subTypes = {
          Connection.class})), mediaType = "application/json")})
  @GetMapping("/agents/{name}")
  public ResponseEntity<?> agents(String agentName) {
    try {
      List<Connection> connections = AgentRegister.getInstance().getConnections(agentName);
      if (connections != null) {
        return ResponseEntity.ok(connections);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Agent not found");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body("An error occurred: " + e.getMessage());
    }
  }

  @Operation(summary = "Get graph")
  @ApiResponse(responseCode = "200", content = {
      @Content(array = @ArraySchema(schema = @Schema(implementation = Graph.class)), mediaType = "application/json")})
  @GetMapping("/graph")
  public Graph getGraph() {

    Agent agent1 = new Agent();
    agent1.setName("agent1");
    agent1.setHost("192.168.0.1");
    agent1.setLastSeenAt(new Date());

    Connection c1 = new Connection();
    c1.setName("C1");
    c1.setHost("192.168.0.3");
    c1.setPort(80);
    c1.setType(ConnectionType.HTTP);
//    c1.setLastCheckedAt(new Date());

    Connection c2 = new Connection();
    c2.setName("C2");
    c2.setHost("192.168.0.4");
    c2.setPort(80);
    c2.setType(ConnectionType.HTTP);
//    c2.setLastCheckedAt(new Date());

    Agent agent2 = new Agent();
    agent2.setName("agent2");
    agent2.setHost("192.168.0.2");
    agent2.setLastSeenAt(new Date());

    Connection c3 = new Connection();
    c3.setName("C3");
    c3.setHost("192.168.0.5");
    c3.setPort(80);
    c3.setType(ConnectionType.HTTP);
//    c3.setLastCheckedAt(new Date());

    agent1.setConnections(Arrays.asList(c1, c2));
    agent2.setConnections(List.of(c3));

//    AgentRegister.getInstance().updateAgent(agent1);
//    AgentRegister.getInstance().updateAgent(agent2);

    return AgentRegister.getInstance().getGraph();
  }
}
