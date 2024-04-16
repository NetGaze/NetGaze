package io.github.amithkoujalgi.netwatch;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Agent agent1 = new Agent();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Agent {
    private String name;
    private String host;
    private List<Connection> connections;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Connection {
    private String name;
    private String source;
    private String target;
    private boolean active;
}