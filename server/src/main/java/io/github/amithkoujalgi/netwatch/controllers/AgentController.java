package io.github.amithkoujalgi.netwatch.controllers;

import io.github.amithkoujalgi.netwatch.eventlistener.Agent;
import io.github.amithkoujalgi.netwatch.eventlistener.AgentRegister;
import io.github.amithkoujalgi.netwatch.eventlistener.Connection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
}