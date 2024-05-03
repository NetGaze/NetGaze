package io.github.amithkoujalgi.netwatch;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    NetWatchAgent agent = new NetWatchAgent("localhost", 8080);
    agent.start();
  }
}

