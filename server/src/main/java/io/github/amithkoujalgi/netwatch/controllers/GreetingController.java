package io.github.amithkoujalgi.netwatch.controllers;

import io.github.amithkoujalgi.netwatch.models.graph.GraphNodeData;
import io.github.amithkoujalgi.netwatch.models.graph.GraphNodePosition;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/ack")
    public GraphNodeData greeting(GraphNodePosition message) throws Exception {
        System.out.println("Received: " + message.getX() + " " + message.getY());
        return new GraphNodeData("Server", "Hello from server!");
    }
}