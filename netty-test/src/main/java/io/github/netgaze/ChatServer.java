package io.github.netgaze;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.client.Socket;

import java.io.UnsupportedEncodingException;

public class ChatServer {
    static private Socket socket;
    static final int PORT = 9291;
    static SocketIOServer server;

    public static void main(String[] args) throws InterruptedException {
        Thread ts = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server();
                } catch (InterruptedException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        ts.start();
    }

    public static void server() throws InterruptedException, UnsupportedEncodingException {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(PORT);
        server = new SocketIOServer(config);
        server.addEventListener("toServer", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toClient", "server received " + data);
            }
        });
        server.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toClient", "message from server " + data);
            }
        });
        server.start();
//        Thread.sleep(10000);
//        server.stop();
    }

}

