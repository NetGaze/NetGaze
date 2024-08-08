package io.github.netgaze.client2;

import lombok.Data;

import java.time.Duration;

@Data
public class GazeStat {
    private int statusCode = -1;
    private Duration responseTime;
    private String errorStackTrace;
    private boolean isActive;
}
