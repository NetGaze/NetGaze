package io.github.amithkoujalgi.netwatch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartupRunner implements ApplicationRunner {
  @Value("${netwatch.event-listener.port}")
  private int port;

  @Override
  public void run(ApplicationArguments args) throws Exception {
//    EventListenerServer eventListenerServer = new EventListenerServer(port);
//    Thread eventListenerServerThread = new Thread(eventListenerServer);
//    eventListenerServerThread.setName("event-listener");
//    eventListenerServerThread.start();
//    eventListenerServerThread.join();
  }
}