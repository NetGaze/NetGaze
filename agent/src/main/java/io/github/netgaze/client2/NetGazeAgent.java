package io.github.netgaze.client2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class NetGazeAgent extends Thread {

    private String configFile;

    private AgentConfig loadAgentConfig() throws IOException {
        return new ObjectMapper(new YAMLFactory()).readValue(new File(configFile), AgentConfig.class);
    }

    public void run() {
        AgentConfig agent = null;
        try {
            agent = loadAgentConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Thread t = new NotifierThread(agent.getAgentHost(), agent.getAgentName(), agent.getServerHost(), agent.getServerPort());
        t.start();
        while (true) {
            if (t.isAlive()) {
                continue;
            } else {
                log.info("Attempting to reconnect...");
                t = new NotifierThread(agent.getAgentHost(), agent.getAgentName(), agent.getServerHost(), agent.getServerPort());
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

