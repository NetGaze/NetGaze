package io.github.netgaze.controllers;

import lombok.Data;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
public class GazeStat {
    private String id = UUID.randomUUID().toString();
    private int statusCode = -1;
    private Duration responseTime;
    private String errorStackTrace;
    private boolean isActive;
    private Instant timestamp;
}
