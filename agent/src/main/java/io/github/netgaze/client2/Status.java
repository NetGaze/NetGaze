package io.github.netgaze.client2;

public enum Status {
    ACTIVE,             // The endpoint is responsive and functioning normally.
    UNRESPONSIVE,       // The endpoint is not responding.
    INACTIVE,           // The endpoint is intentionally not active.
    ERROR,              // The endpoint encountered an error during the request.
    DEGRADED,           // The endpoint is operational but with reduced performance/high latency.
    MAINTENANCE,        // The endpoint is temporarily down for maintenance.
    OFFLINE,            // The endpoint is completely unreachable.
}
