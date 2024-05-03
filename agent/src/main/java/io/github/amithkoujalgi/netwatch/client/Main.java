package io.github.amithkoujalgi.netwatch.client;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        NetWatchAgent netWatchAgent = new NetWatchAgent(
                "localhost",
                8080,
                "/Users/amithkoujalgi/Downloads/agent-config.yaml"
        );

        // this starts NetWatch agent in the background

        netWatchAgent.start();

        netWatchAgent.join();
    }
}
