package io.github.amithkoujalgi.netwatch;

import io.github.amithkoujalgi.netwatch.client.NetWatchAgent;
import java.util.Collections;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Connection c = new Connection();
    c.setName("C1");
    c.setHost("google.com");
    c.setPort(80);
    c.setType(ConnectionType.HTTP);
    NetWatchAgent netWatchAgent = new NetWatchAgent("localhost", 8990, "Agent1",
        Collections.singletonList(c));
    netWatchAgent.start();
    netWatchAgent.join();
  }
}

