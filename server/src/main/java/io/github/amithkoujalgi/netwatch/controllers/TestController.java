package io.github.amithkoujalgi.netwatch.controllers;

import io.github.amithkoujalgi.netwatch.eventlistener.Agent;
import io.github.amithkoujalgi.netwatch.eventlistener.AgentRegister;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "", description = "Test APIs")
@RestController
public class TestController {


    @Operation(summary = "Test")
    @ApiResponse(responseCode = "200", content = {
            @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)), mediaType = "application/json")})
    @GetMapping("/agents")
    public List<Agent> agents() {
        return AgentRegister.getInstance().getAgents();
    }
}
