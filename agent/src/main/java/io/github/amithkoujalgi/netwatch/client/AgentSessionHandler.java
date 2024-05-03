package io.github.amithkoujalgi.netwatch.client;

import io.github.amithkoujalgi.netwatch.Ack;
import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.github.amithkoujalgi.netwatch.ConnectionType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Getter
@SuppressWarnings("NullableProblems")
public class AgentSessionHandler extends StompSessionHandlerAdapter {

  private boolean disconnected = false;

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    String topic = "/topic/ack";
    System.out.println("New session established: " + session.getSessionId());
    session.subscribe(topic, this);
    System.out.println("Subscribed to " + topic);
    new Thread(() -> {
      while (!disconnected) {
        try {
          session.send("/app/event-listener", getSampleMessage());
          System.out.println("Message sent to websocket server");
        } catch (IllegalStateException e) {
          System.out.println("Error: " + e.getMessage());
          break;
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
      System.out.println("Terminated thread.");
    }).start();
  }


  @Override
  public void handleException(StompSession session, StompCommand command, StompHeaders headers,
      byte[] payload, Throwable exception) {
    System.out.println(exception.getMessage());
  }


  @Override
  public Type getPayloadType(StompHeaders headers) {
    return Ack.class;
  }


  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    Ack msg = (Ack) payload;
    System.out.println("Received from server: " + msg.getMessage());
  }


  @Override
  public void handleTransportError(StompSession session, Throwable exception) {
    System.out.println("Disconnected Transport error: " + exception.getMessage());
    // Handle disconnection here, e.g., reconnect or close resources.
    disconnected = true;
  }

  private Agent getSampleMessage() {
    Agent msg = new Agent();
    msg.setHost("0.1.2.3");
    msg.setName("Test Agent");
    msg.setLastSeenAt(new Date());

    Connection c = new Connection();
    c.setName("C1");
    c.setHost("google.com");
    c.setPort(80);
    c.setType(ConnectionType.HTTP);

    msg.setConnections(List.of(c));
    return msg;
  }
}
