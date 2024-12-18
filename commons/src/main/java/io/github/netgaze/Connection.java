package io.github.netgaze;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

    private String name;
    private String description;
    private ConnectionType type;
    private String host;
    private int port;
    private boolean active;
    private Instant lastCheckedAt;
}
