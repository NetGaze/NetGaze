package io.github.amithkoujalgi.netwatch;

import io.github.amithkoujalgi.netwatch.client.AgentSessionHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SuppressWarnings({"BusyWait", "InfiniteLoopStatement"})
@Data
@AllArgsConstructor
public class NetWatchAgent extends Thread {

  private String host;
  private int port;

  public void run() {
    Thread t = getNotifierThread(host, port);
    t.start();
    while (true) {
      if (t.isAlive()) {
        continue;
      } else {
        System.out.println("Attempting to reconnect...");
        t = getNotifierThread(host, port);
        t.start();
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Thread getNotifierThread(String host, int port) {
    return new Thread(() -> {
      WebSocketClient client = new StandardWebSocketClient();
      WebSocketStompClient stompClient = new WebSocketStompClient(client);
      stompClient.setMessageConverter(new MappingJackson2MessageConverter());
      AgentSessionHandler sessionHandler = new AgentSessionHandler();
      stompClient.connectAsync(
          String.format("ws://%s:%s/netwatch-agent-event-listener", host, port), sessionHandler);
      while (true) {
        boolean disconnected = sessionHandler.isDisconnected();
        if (disconnected) {
          break;
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}

