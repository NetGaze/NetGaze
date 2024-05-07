package io.github.amithkoujalgi.netwatch.controllers;

import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.github.amithkoujalgi.netwatch.ConnectionType;
import io.github.amithkoujalgi.netwatch.core.AgentRegister;
import io.github.amithkoujalgi.netwatch.models.graph.Graph;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        return AgentRegister.getInstance().getGraph();
    }
}
