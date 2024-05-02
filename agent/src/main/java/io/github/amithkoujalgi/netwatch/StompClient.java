package io.github.amithkoujalgi.netwatch;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StompClient {

    private static final String URL = "ws://localhost:8080/gs-guide-websocket";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new MyStompSessionHandler();
        stompClient.connect(URL, sessionHandler);
        new Scanner(System.in).nextLine(); // Don't close immediately.
    }
}

class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @SuppressWarnings("NullableProblems")
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        String topic = "/topic/ack";
        System.out.println("New session established: " + session.getSessionId());
        session.subscribe(topic, this);
        System.out.println("Subscribed to " + topic);
        new Thread(() -> {
            while (true) {
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

        }).start();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println(exception.getMessage());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Ack.class;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        Ack msg = (Ack) payload;
        System.out.println("Received from server: " + msg.getMessage());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Disconnected Transport error: " + exception.getMessage());
        // Handle disconnection here, e.g., reconnect or close resources.
    }

    /**
     * A sample message instance.
     *
     * @return instance of <code>Message</code>
     */
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
