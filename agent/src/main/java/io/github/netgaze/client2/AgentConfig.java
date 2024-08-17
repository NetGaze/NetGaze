package io.github.netgaze.client2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentConfig {
    private String agentName;
    private String agentHost;
    private String serverHost;
    private int serverPort;
    private Instant lastSeenAt;
    private List<Connection> connections = new ArrayList<>();
}
