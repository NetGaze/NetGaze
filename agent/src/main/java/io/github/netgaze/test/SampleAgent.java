package io.github.netgaze.test;

import io.github.netgaze.Connection;
import io.github.netgaze.Scheme;
import io.github.netgaze.client2.NetGazeAgent;

public class SampleAgent {
    public static void main(String[] args) throws InterruptedException {
//        NetGazeAgent netGazeAgent = new NetGazeAgent(
//                "localhost",
//                8080,
//                "/Users/amithkoujalgi/NetGaze/agent-config.yaml"
//        );
//
//        // this starts NetGaze agent in the background
//
//        netGazeAgent.start();
//
//        netGazeAgent.join();

        Connection c = new Connection();
        c.setHost("localhost");
        c.setPort(11434);
        c.setScheme(Scheme.HTTP);


//        Gazer g = new Gazer(Collections.singletonList(c));
//        g.start();
//        g.start();
//        Thread.sleep(12000);
//        g.stop();
//        g.stop();

        NetGazeAgent agent = new NetGazeAgent("/Users/amithkoujalgi/NetGaze/agent-config.yaml");
        agent.start();
    }
}
