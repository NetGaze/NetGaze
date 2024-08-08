package io.github.netgaze.client2;

import java.util.ArrayDeque;
import java.util.Deque;

public class GazeHistoryManager {
    private static final int MAX_SIZE = 100;
    private final Deque<GazeStat> gazeHistory = new ArrayDeque<>(MAX_SIZE);

    public GazeHistoryManager() {
    }

    public synchronized void addStat(GazeStat stat) {
        if (gazeHistory.size() >= MAX_SIZE) {
            gazeHistory.removeFirst();
        }
        gazeHistory.addLast(stat);
    }

    public synchronized Deque<GazeStat> getGazeHistory() {
        return new ArrayDeque<>(gazeHistory);
    }
}
