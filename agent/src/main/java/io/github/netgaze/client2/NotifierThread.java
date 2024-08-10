package io.github.netgaze.client2;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class NotifierThread extends Thread {

    private final String agentHost;
    private final String agentName;

    private final String serverHost;
    private final int serverPort;


    public NotifierThread(String agentHost, String agentName, String serverHost, int serverPort) {
        this.agentHost = agentHost;
        this.agentName = agentName;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        AgentSessionHandler sessionHandler = new AgentSessionHandler(agentHost, agentName);
        stompClient.connectAsync(String.format("ws://%s:%s/netgaze-agent-event-listener", serverHost, serverPort),
                sessionHandler);
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
    }
}
