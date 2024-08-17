package io.github.netgaze.client2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentInfo {
    private String name;
    private String host;
    private Instant lastSeenAt;
    private Map<Connection, Deque<GazeStat>> connectionStats = new HashMap<>();
}
