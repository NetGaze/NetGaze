package io.github.netgaze.client2;

import java.util.ArrayDeque;
import java.util.Deque;

public class GazeHistoryManager {
    private final int maxSize;
    private final Deque<GazeStat> gazeHistory;

    public GazeHistoryManager(int maxSize) {
        this.maxSize = maxSize;
        this.gazeHistory = new ArrayDeque<>(maxSize);
    }

    public synchronized void addStat(GazeStat stat) {
        if (gazeHistory.size() >= maxSize) {
            gazeHistory.removeFirst();
        }
        gazeHistory.addLast(stat);
    }

    public synchronized Deque<GazeStat> getGazeHistory() {
        return new ArrayDeque<>(gazeHistory);
    }
}
