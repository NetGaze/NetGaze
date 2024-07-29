package io.github.netgaze.test;

import io.github.netgaze.client.NetGazeAgent;

public class SampleAgent {
    public static void main(String[] args) throws InterruptedException {
        NetGazeAgent netGazeAgent = new NetGazeAgent(
                "localhost",
                8080,
                "/Users/amithkoujalgi/NetGaze/agent-config.yaml"
        );

        // this starts NetGaze agent in the background

        netGazeAgent.start();

        netGazeAgent.join();
    }
}
