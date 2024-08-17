package io.github.netgaze.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public
class CAgent {
    private String name;
    private String host;
    private Instant lastSeenAt;
    private List<CStats> agentStats = new ArrayList<>();
}
