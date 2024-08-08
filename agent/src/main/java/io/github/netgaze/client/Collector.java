//package io.github.netgaze.client;
//
//import io.github.netgaze.Agent;
//import io.github.netgaze.Connection;
//import io.github.netgaze.Scheme;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.net.HttpURLConnection;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//import java.net.URL;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//@Slf4j
//@AllArgsConstructor
//public class Collector {
//
//    private Agent agent;
//
//    public void gather() {
//        // Create a thread pool with a fixed number of threads
//        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//
//        // List to hold Future objects for tracking the completion of tasks
//        List<Future<Void>> futures = new ArrayList<>();
//
//        try {
//            // Submit tasks to the executor service
//            for (Connection connection : this.agent.getConnections()) {
//                Callable<Void> task = () -> {
//                    boolean status = false;
//                    String url = "";
//                    try {
//                        if (connection.getScheme().equals(Scheme.HTTP)) {
//                            url = String.format("http://%s:%d", connection.getHost(), connection.getPort());
//                            status = checkHTTPConnection(url);
//                        } else if (connection.getScheme().equals(Scheme.HTTPS)) {
//                            url = String.format("https://%s:%d", connection.getHost(), connection.getPort());
//                            status = checkHTTPSConnection(url);
//                        } else if (connection.getScheme().equals(Scheme.TCP)) {
//                            url = String.format("%s:%d", connection.getHost(), connection.getPort());
//                            status = checkTCPConnection(connection.getHost(), connection.getPort());
//                        }
//                        connection.setActive(status);
//                        connection.setCheckedAt(Instant.now()); // Capture the time in UTC
//
//                        // Log the status
//                        log.info("Checked {0}: Status = {1}", new Object[]{url, status});
//                    } catch (Exception e) {
//                        // Log the exception
//                        log.error("Error checking connection for {}", url, e);
//                    }
//                    return null; // Callable tasks must return a value, but we don’t need one
//                };
//                futures.add(executorService.submit(task));
//            }
//
//            // Wait for all tasks to complete
//            for (Future<Void> future : futures) {
//                try {
//                    future.get(); // This will block until the task completes
//                } catch (Exception e) {
//                    // Handle exceptions as appropriate
//                    log.error("Error waiting for task completion", e);
//                }
//            }
//        } finally {
//            // Shut down the executor service
//            executorService.shutdown();
//        }
//    }
//
//
//    private boolean checkHTTPConnection(String urlStr) {
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            connection.setConnectTimeout(5000);
//            int responseCode = connection.getResponseCode();
//            boolean result = responseCode == HttpURLConnection.HTTP_OK;
//            log.info("HTTP connection to {} is {}", urlStr, result ? "active" : "inactive");
//            return result;
//        } catch (IOException e) {
//            log.error("Error checking HTTP connection: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    private boolean checkHTTPSConnection(String urlStr) {
//        try {
//            URL url = new URL(urlStr);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            connection.setConnectTimeout(5000);
//            int responseCode = connection.getResponseCode();
//            boolean result = responseCode == HttpURLConnection.HTTP_OK;
//            log.info("HTTPS connection to {} is {}", urlStr, result ? "active" : "inactive");
//            return result;
//        } catch (IOException e) {
//            log.error("Error checking HTTPS connection: {}", e.getMessage());
//            return false;
//        }
//    }
//
//    private boolean checkTCPConnection(String host, int port) {
//        try (Socket socket = new Socket()) {
//            socket.connect(new InetSocketAddress(host, port), 1000); // Timeout in milliseconds
//            boolean result = socket.isConnected();
//            log.info("TCP connection to {}:{} is {}", host, port, result ? "active" : "inactive");
//            return result;
//        } catch (IOException e) {
//            log.error("Error checking TCP connection: {}", e.getMessage());
//            return false;
//        }
//    }
//}
