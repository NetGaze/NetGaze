//package io.github.netgaze.client;
//
//import io.github.netgaze.Agent;
//import org.springframework.messaging.converter.MappingJackson2MessageConverter;
//import org.springframework.web.socket.client.WebSocketClient;
//import org.springframework.web.socket.client.standard.StandardWebSocketClient;
//import org.springframework.web.socket.messaging.WebSocketStompClient;
//
//public class NotifierThread extends Thread {
//
//    private final String host;
//    private final int port;
//    private final Agent agent;
//
//    public NotifierThread(String host, int port, Agent agent) {
//        this.host = host;
//        this.port = port;
//        this.agent = agent;
//    }
//
//    @Override
//    public void run() {
//        WebSocketClient client = new StandardWebSocketClient();
//        WebSocketStompClient stompClient = new WebSocketStompClient(client);
//        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
//        AgentSessionHandler sessionHandler = new AgentSessionHandler(agent);
//        stompClient.connectAsync(String.format("ws://%s:%s/netgaze-agent-event-listener", host, port),
//                sessionHandler);
//        while (true) {
//            boolean disconnected = sessionHandler.isDisconnected();
//            if (disconnected) {
//                break;
//            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
