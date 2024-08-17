package io.github.netgaze.controllers;

import io.github.netgaze.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Tag(name = "", description = "Agent APIs")
@RestController
public class AgentController {
    @Autowired
    private AgentService agentService;

    @Operation(summary = "List agents")
    @ApiResponse(responseCode = "200", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = List.class, subTypes = {
                    CAgent.class})), mediaType = "application/json")})
    @GetMapping("/agents")
    public ResponseEntity<List<CAgent>> agents() {

        return ResponseEntity.ok(new ArrayList<>());
//        return ResponseEntity.ok(agentService.listAgents());
    }
}


