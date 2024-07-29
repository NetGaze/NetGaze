package io.github.netgaze.client;

import io.github.netgaze.Agent;
import io.github.netgaze.Connection;
import io.github.netgaze.ConnectionType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Date;

@Slf4j
@AllArgsConstructor
public class Collector {

    private Agent agent;

    public void gather() {
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

    private boolean checkHTTPConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            connection.setConnectTimeout(5000);
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

    private boolean checkHTTPSConnection(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            connection.setConnectTimeout(5000);
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

    private boolean checkTCPConnection(String host, int port) {
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
