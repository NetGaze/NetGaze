package io.github.netgaze.client2;

import io.github.netgaze.Connection;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Gazer {
    private static final int THREAD_POOL_SIZE = 3;

    private final List<Connection> connections;

    public Gazer(List<Connection> connections) {
        this.connections = connections;
    }

    private final List<Gazelet> gazelets = new ArrayList<>();


    public void start() {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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


    public void stop() {
        for (Gazelet gazelet : gazelets) {
            gazelet.shutdown();
        }
    }
}

