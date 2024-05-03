package io.github.amithkoujalgi.netwatch.client;

import io.github.amithkoujalgi.netwatch.Ack;
import io.github.amithkoujalgi.netwatch.Agent;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

@Getter
public class AgentSessionHandler extends StompSessionHandlerAdapter {
    private final Agent agent;

    private boolean disconnected = false;

    public AgentSessionHandler(Agent agent) {
        this.agent = agent;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        String topic = "/topic/ack";
        System.out.println("New session established: " + session.getSessionId());
        session.subscribe(topic, this);
        System.out.println("Subscribed to " + topic);
        new Thread(() -> {
            while (!disconnected) {
                try {
                    session.send("/app/event-listener", agent);
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
}
