package io.github.amithkoujalgi.netwatch;

import lombok.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.Scanner;

public class StompClient {

    private static String URL = "ws://localhost:8080/gs-guide-websocket";

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
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("New session established: " + session.getSessionId());
        session.subscribe("/topic/ack", this);
        System.out.println("Subscribed to /topic/greetings");
        session.send("/app/hello", getSampleMessage());
        System.out.println("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println(exception.getMessage());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GraphNodeData.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GraphNodeData msg = (GraphNodeData) payload;
        System.out.println("Received from server: " + msg.getId() + ": " + msg.getParent());
    }

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
    private GraphNodePosition getSampleMessage() {
        GraphNodePosition msg = new GraphNodePosition();
        msg.setX(1);
        msg.setY(1);
        return msg;
    }
}

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class GraphNodePosition {

    private int x;
    private int y;
}

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
class GraphNodeData {

    private String id;
    private String parent;
}
