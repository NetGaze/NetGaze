package io.github.netgaze.client2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.Duration;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class NetGazeAgent extends Thread {

    private String configFile;

    private AgentConfig loadAgentConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.registerModule(new JavaTimeModule()
                .addDeserializer(Duration.class, new DurationDeserializer()));
        return mapper.readValue(new File(configFile), AgentConfig.class);
    }

    public void run() {
        AgentConfig agent = null;
        Gazer gazer = null;
        try {
            agent = loadAgentConfig();
            gazer = new Gazer(agent.getConnections());
            gazer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread t = new NotifierThread(agent.getAgentHost(), agent.getAgentName(), agent.getServerHost(), agent.getServerPort(), gazer);
        t.start();
        while (true) {
            if (t.isAlive()) {
                continue;
            } else {
                log.info("Attempting to reconnect...");
                t = new NotifierThread(agent.getAgentHost(), agent.getAgentName(), agent.getServerHost(), agent.getServerPort(), gazer);
                t.start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}