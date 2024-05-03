package io.github.amithkoujalgi.netwatch.client;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String configFile = "/Users/amithkoujalgi/Downloads/agent-config.yaml";
        NetWatchAgent netWatchAgent = new NetWatchAgent("localhost", 8080, configFile);
        netWatchAgent.start();
    }
}
