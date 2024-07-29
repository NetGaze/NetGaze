package io.github.netgaze.client;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NetGazeAgent netGazeAgent = new NetGazeAgent(
                "localhost",
                8080,
                "/Users/amithkoujalgi/Downloads/agent-config.yaml"
        );

        // this starts NetGaze agent in the background

        netGazeAgent.start();

        netGazeAgent.join();
    }
}
