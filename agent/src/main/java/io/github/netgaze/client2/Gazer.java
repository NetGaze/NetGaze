package io.github.netgaze.client2;

import io.github.netgaze.Connection;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Gazer {
    @Setter
    private static int threadPoolSize = 3;

    private final List<Connection> connections;
    private ExecutorService executor;

    public Gazer(List<Connection> connections) {
        this.connections = connections;
    }

    @SuppressWarnings("SpellCheckingInspection")
    private final List<Gazelet> gazelets = new ArrayList<>();


    public void start() {
        if (executor != null && !executor.isShutdown()) {
            log.info("Gazer has already started.");
            return;
        }
        log.info("Starting Gazer...");
        executor = Executors.newFixedThreadPool(threadPoolSize);
        try {
            for (Connection connection : connections) {
                Gazelet gazelet = new Gazelet(connection);
                executor.submit(() -> {
                    try {
                        gazelet.start();
                    } catch (Exception e) {
                        log.error("Error starting Gazelet. ", e);
                    }
                });
                gazelets.add(gazelet);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }


    public Deque<GazeStat> getGazeHistory(Connection connection) {
        for (Gazelet gazelet : gazelets) {
            if (gazelet.getConnection().equals(connection)) {
                return gazelet.getGazeHistory();
            }
        }
        throw new IllegalStateException("No Gazelet found for connection " + connection);
    }

    public void stop() throws InterruptedException {
        if (executor == null || executor.isShutdown() || executor.isTerminated()) {
            log.info("Could not stop Gazer since it isn't active.");
            return;
        }
        log.info("Stopping all Gazelets...");
        for (Gazelet gazelet : gazelets) {
            gazelet.requestShutdown();
        }
        executor.shutdown();
        try {
            log.info("Stopping Gazer...");
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            log.info("Gazer stopped.");
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
            throw e;
        }
    }
}

