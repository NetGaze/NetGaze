package io.github.amithkoujalgi.netwatch;

import io.github.amithkoujalgi.netwatch.client.AgentSessionHandler;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

@SuppressWarnings("BusyWait")
public class StompClient {

  private static final String URL = "ws://localhost:8080/netwatch-agent-event-listener";

  public static void main(String[] args) {
    Thread t = getNotifierThread();
    t.start();
    while (true) {
      if (t.isAlive()) {
        continue;
      } else {
        System.out.println("Attempting to reconnect...");
        t = getNotifierThread();
        t.start();
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static Thread getNotifierThread() {
    return new Thread(() -> {
      WebSocketClient client = new StandardWebSocketClient();
      WebSocketStompClient stompClient = new WebSocketStompClient(client);
      stompClient.setMessageConverter(new MappingJackson2MessageConverter());
      AgentSessionHandler sessionHandler = new AgentSessionHandler();
      stompClient.connectAsync(URL, sessionHandler);
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

