package io.github.netgaze.client2;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
public class AgentSessionHandler extends StompSessionHandlerAdapter {

    private boolean disconnected = false;

    private Gazer gazer;

    private final String agentHost;
    private final String agentName;

    public AgentSessionHandler(String agentHost, String agentName, Gazer gazer) {
        this.agentHost = agentHost;
        this.agentName = agentName;
        this.gazer = gazer;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established: {}", session.getSessionId());
        new Thread(() -> {
            while (!disconnected) {
                try {
//                    AgentInfo agent = new AgentInfo();
//                    agent.setConnectionStats(gazer.getGazeHistory());
//                    agent.setHost(agentHost);
//                    agent.setName(agentName);
//                    agent.setLastSeenAt(Instant.now());

                    CAgent agent = new CAgent();
                    agent.setName(agentName);
                    agent.setHost(agentHost);
                    agent.setLastSeenAt(Instant.now());

                    for (Map.Entry<Connection, Deque<GazeStat>> entry : gazer.getGazeHistory().entrySet()) {
                        List<GazeStat> gazeStats = entry.getValue().stream().toList();
                        agent.getAgentStats().add(new CStats(entry.getKey(), gazeStats));
                    }


                    String agentStr = ObjectMapperProvider.getObjectMapper().writeValueAsString(agent);
                    session.send("/app/event-listener", agentStr);
                    log.info("Published event to server: {}", agentStr);
                } catch (IllegalStateException | JsonProcessingException e) {
                    log.info("Error: {}", e.getMessage());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.error("Terminated publisher.");
        }).start();
    }


    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers,
                                byte[] payload, Throwable exception) {
        log.error(exception.getMessage());
    }


    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        log.error("Transport error: {}", exception.getMessage());
        disconnected = true;
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class CAgent {
    private String name;
    private String host;
    private Instant lastSeenAt;
    private List<CStats> agentStats = new ArrayList<>();
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class CStats {
    private Connection connection;
    private List<GazeStat> gazeStats = new ArrayList<>();
}
