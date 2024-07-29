package io.github.netgaze.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.github.netgaze.Agent;
import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Slf4j
public class NetGazeAgent extends Thread {

  private String host;
  private int port;
  private String configFile;

  private Agent loadAgentConfig() throws IOException {
    return new ObjectMapper(new YAMLFactory()).readValue(new File(configFile), Agent.class);
  }

  public void run() {
    Agent agent = null;
    try {
      agent = loadAgentConfig();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Thread t = getNotifierThread(host, port, agent);
    t.start();
    while (true) {
      if (t.isAlive()) {
        continue;
      } else {
        log.info("Attempting to reconnect...");
        t = getNotifierThread(host, port, agent);
        t.start();
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private Thread getNotifierThread(String host, int port, Agent agent) {
    return new NotifierThread(host, port, agent);
  }
}

