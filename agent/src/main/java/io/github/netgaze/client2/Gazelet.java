package io.github.netgaze.client2;

import io.github.netgaze.Connection;
import io.github.netgaze.Scheme;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Gazelet {

    @NonNull
    @Getter
    private final Connection connection;

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    @Getter
    private boolean isShutdownRequested = false;

    private final GazeHistoryManager historyManager;

    private ScheduledExecutorService executorService;

    public Gazelet(@NonNull Connection connection, int maxHistory) {
        this.connection = connection;
        if (this.connection.getTimeout() == null) {
            this.connection.setTimeout(DEFAULT_TIMEOUT);
        }
        this.historyManager = new GazeHistoryManager(maxHistory);
    }

    public synchronized void start() {
        log.info("Starting Gazelet for {}: {}", this.connection.getHost(), this.connection.getPort());
        long initialDelay = connection.getPollInterval().toMillis();
        long period = connection.getPollInterval().toMillis();
        this.isShutdownRequested = false;
        if (executorService == null || executorService.isShutdown()) {
            this.executorService = Executors.newSingleThreadScheduledExecutor();
        }
        executorService.scheduleAtFixedRate(this::checkConnection, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public void requestShutdown() throws InterruptedException {
        log.info("Requesting Gazelet to stop...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.isShutdownRequested = true;
        shutdown();
    }

    private void shutdown() throws InterruptedException {
        log.info("Stopping Gazelet for {}: {}...", this.connection.getHost(), this.connection.getPort());
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(this.connection.getPollInterval().toSeconds(), TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
            log.info(" Gazelet for {}: {} stopped.", this.connection.getHost(), this.connection.getPort());
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    public Deque<GazeStat> getGazeHistory() {
        return historyManager.getGazeHistory();
    }

    private synchronized void checkConnection() {
        if (this.isShutdownRequested) {
            return;
        }
        String endpoint = "";
        try {
            GazeStat stat = null;
            if (connection.getScheme().equals(Scheme.HTTP)) {
                endpoint = String.format("http://%s:%d", connection.getHost(), connection.getPort());
                stat = checkHTTPConnection(endpoint);
            } else if (connection.getScheme().equals(Scheme.HTTPS)) {
                endpoint = String.format("https://%s:%d", connection.getHost(), connection.getPort());
                stat = checkHTTPSConnection(endpoint);
            } else if (connection.getScheme().equals(Scheme.TCP)) {
                endpoint = String.format("%s:%d", connection.getHost(), connection.getPort());
                stat = checkTCPConnection(connection.getHost(), connection.getPort());
            }
            stat.setTimestamp(Instant.now());

            log.info("Checked endpoint '{}': Status Code = {}, Response Time = {} ms, Status = {}",
                    endpoint,
                    stat.getStatusCode(),
                    stat.getResponseTime() == null ? -1 : stat.getResponseTime().toMillis(),
                    stat.isActive() ? "active" : "inactive"
            );
            historyManager.addStat(stat);
        } catch (Exception e) {
            log.error("Error checking connection for '{}'", endpoint, e);
        }
    }

    private GazeStat checkHTTPConnection(String urlStr) {
        GazeStat stat = new GazeStat();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout((int) this.connection.getTimeout().toMillis());
            Instant startTime = Instant.now();
            connection.connect();

            int statusCode = connection.getResponseCode();
            Instant endTime = Instant.now();
            Duration responseTime = Duration.between(startTime, endTime);
            boolean result = statusCode == HttpURLConnection.HTTP_OK;
            log.info("HTTP connection to '{}': Status Code = {}, Result = {}, Response Time = {} ms",
                    urlStr,
                    statusCode,
                    result ? "active" : "inactive",
                    responseTime.toMillis());
            stat.setActive(result);
            stat.setResponseTime(responseTime);
            stat.setStatusCode(statusCode);
        } catch (IOException e) {
            stat.setErrorStackTrace(getStackTrace(e));
            stat.setActive(false);
            log.error("Error checking HTTP connection to '{}'", urlStr, e);
        }
        return stat;
    }

    private GazeStat checkHTTPSConnection(String urlStr) {
        GazeStat stat = new GazeStat();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout((int) this.connection.getTimeout().toMillis());
            Instant startTime = Instant.now();
            connection.connect();
            int statusCode = connection.getResponseCode();
            Instant endTime = Instant.now();
            Duration responseTime = Duration.between(startTime, endTime);
            boolean result = statusCode == HttpURLConnection.HTTP_OK;
            log.info("HTTPS connection to '{}': Status Code = {}, Result = {}, Response Time = {} ms",
                    urlStr,
                    statusCode,
                    result ? "active" : "inactive",
                    responseTime.toMillis());
            stat.setActive(result);
            stat.setResponseTime(responseTime);
            stat.setStatusCode(statusCode);
        } catch (IOException e) {
            stat.setErrorStackTrace(getStackTrace(e));
            stat.setActive(false);
            log.error("Error checking HTTPS connection to '{}'", urlStr, e);
        }
        return stat;
    }

    private GazeStat checkTCPConnection(String host, int port) {
        GazeStat stat = new GazeStat();
        int statusCode = -1;
        try {
            Instant startTime = Instant.now();
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, port), (int) this.connection.getTimeout().toMillis());
                Instant endTime = Instant.now();
                Duration responseTime = Duration.between(startTime, endTime);
                boolean result = socket.isConnected();
                log.info("TCP connection to '{}:{}': Result = {}, Response Time = {} ms",
                        host,
                        port,
                        result ? "active" : "inactive",
                        responseTime.toMillis());
                stat.setActive(result);
                stat.setResponseTime(responseTime);
                stat.setStatusCode(statusCode);
            }
        } catch (IOException e) {
            stat.setErrorStackTrace(getStackTrace(e));
            stat.setActive(false);
            log.error("Error checking TCP connection to '{}:{}'", host, port, e);
        }
        return stat;
    }

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
        }
        return sw.toString();
    }

}

