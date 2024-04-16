package io.github.amithkoujalgi.netwatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Agent agent = new Agent();

        Connection c = new Connection();
        c.setHost("google.com");
        c.setPort(80);
        c.setType(ConnectionType.HTTP);
        agent.setConnections(Collections.singletonList(c));

        agent.checkConnections();
        System.out.printf(String.valueOf(c.isActive()));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Agent {
    private String name;
    private String host;
    @Setter
    private List<Connection> connections;

    public void checkConnections() {
        for (Connection connection : connections) {
            if (connection.getType().equals(ConnectionType.HTTP)) {
                boolean status = checkHTTPConnection(String.format("http://%s:%d", connection.getHost(), connection.getPort()));
                connection.setActive(status);
            } else if (connection.getType().equals(ConnectionType.HTTPS)) {
                boolean status = checkHTTPSConnection(String.format("https://%s:%d", connection.getHost(), connection.getPort()));
                connection.setActive(status);
            } else if (connection.getType().equals(ConnectionType.TCP)) {
                boolean status = checkTCPConnection(connection.getHost(), connection.getPort());
                connection.setActive(status);
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
            System.out.println("HTTP connection to " + urlStr + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            System.out.println("Error checking HTTP connection: " + e.getMessage());
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
            System.out.println("HTTPS connection to " + urlStr + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            System.out.println("Error checking HTTPS connection: " + e.getMessage());
            return false;
        }
    }

    private static boolean checkTCPConnection(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000); // Timeout in milliseconds
            boolean result = socket.isConnected();
            System.out.println("TCP connection to " + host + ":" + port + " is " + (result ? "active" : "inactive"));
            return result;
        } catch (IOException e) {
            System.out.println("Error checking TCP connection: " + e.getMessage());
            return false;
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Connection {
    @Setter
    private String name;
    @Setter
    private ConnectionType type;
    @Setter
    private String host;
    @Setter
    private int port;
    @Setter
    private boolean active;
    @Setter
    private Data lastCheckedAt;
}

enum ConnectionType {
    HTTP, HTTPS, TCP
}