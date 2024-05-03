package io.github.amithkoujalgi.netwatch.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.amithkoujalgi.netwatch.Agent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class NetWatchAgent extends Thread {

    private String host;
    private int port;
    private String configFile;

    private Agent loadAgentConfig() throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(new File(configFile), Agent.class);
    }

    public void run() {
        Agent agent = null;
        try {
            agent = loadAgentConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread t = getNotifierThread(host, port, agent);
        t.start();
        while (true) {
            if (t.isAlive()) {
                continue;
            } else {
                System.out.println("Attempting to reconnect...");
                t = getNotifierThread(host, port, agent);
                t.start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Thread getNotifierThread(String host, int port, Agent agent) {
        return new NotifierThread(host, port, agent);
    }
}

class NotifierThread extends Thread {
    private final String host;
    private final int port;
    private Agent agent;

    public NotifierThread(String host, int port, Agent agent) {
        this.host = host;
        this.port = port;
        this.agent = agent;
    }

    @Override
    public void run() {
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        AgentSessionHandler sessionHandler = new AgentSessionHandler(agent);
        stompClient.connectAsync(String.format("ws://%s:%s/netwatch-agent-event-listener", host, port), sessionHandler);
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

