package io.github.netgaze.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.netgaze.Agent;
import io.github.netgaze.util.ObjectMapperProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

@Getter
@Slf4j
public class AgentSessionHandler extends StompSessionHandlerAdapter {

    private final Agent agent;

    private boolean disconnected = false;

    public AgentSessionHandler(Agent agent) {
        this.agent = agent;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established: " + session.getSessionId());
        Collector collector = new Collector(agent);
        new Thread(() -> {
            while (!disconnected) {
                try {
                    collector.gather();
                    session.send("/app/event-listener", ObjectMapperProvider.getObjectMapper().writeValueAsString(agent));
                    log.info("Published event to server");
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
        log.error("Transport error: " + exception.getMessage());
        disconnected = true;
    }
}

