package io.github.amithkoujalgi.netwatch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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
    private Date lastCheckedAt;
}
