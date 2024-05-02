package io.github.amithkoujalgi.netwatch.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.github.amithkoujalgi.netwatch.ConnectionType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@SuppressWarnings({"InfiniteLoopStatement", "BusyWait"})
public class EventSender implements Runnable {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final ChannelHandlerContext ctx;
    private Agent agent;

    public EventSender(Agent agent, ChannelHandlerContext ctx) {
        this.agent = agent;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        while (true) {
            try {
                gather();
                String msg = objectMapper.writeValueAsString(agent);
                log.info("Sending event to server...");
                ctx.writeAndFlush(getMessage(msg));
                Thread.sleep(1000);
            } catch (RejectedExecutionException e) {
                log.error("error sending");
                break;
            } catch (Exception e) {
                System.out.println("ERRR");
            }
        }
    }

    private ByteBuf getMessage(String message) {
        return Unpooled.copiedBuffer(message, StandardCharsets.UTF_8);
    }

    private void gather() {
        for (Connection connection : this.agent.getConnections()) {
            if (connection.getType().equals(ConnectionType.HTTP)) {
                boolean status = checkHTTPConnection(
                        String.format("http://%s:%d", connection.getHost(), connection.getPort()));
                connection.setActive(status);
                connection.setLastCheckedAt(new Date());
            } else if (connection.getType().equals(ConnectionType.HTTPS)) {
                boolean status = checkHTTPSConnection(
                        String.format("https://%s:%d", connection.getHost(), connection.getPort()));
                connection.setActive(status);
                connection.setLastCheckedAt(new Date());
            } else if (connection.getType().equals(ConnectionType.TCP)) {
                boolean status = checkTCPConnection(connection.getHost(), connection.getPort());
                connection.setActive(status);
                connection.setLastCheckedAt(new Date());
            }
        }
    }

    private static boolean checkHTTPConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            boolean result = responseCode == HttpURLConnection.HTTP_OK;
            log.info(
                    "HTTP connection to " + urlStr + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            log.error("Error checking HTTP connection: " + e.getMessage());
            return false;
        }
    }

    private static boolean checkHTTPSConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            boolean result = responseCode == HttpURLConnection.HTTP_OK;
            log.info(
                    "HTTPS connection to " + urlStr + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            log.error("Error checking HTTPS connection: " + e.getMessage());
            return false;
        }
    }

    private static boolean checkTCPConnection(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000); // Timeout in milliseconds
            boolean result = socket.isConnected();
            log.info(
                    "TCP connection to " + host + ":" + port + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            log.error("Error checking TCP connection: " + e.getMessage());
            return false;
        }
    }
}
