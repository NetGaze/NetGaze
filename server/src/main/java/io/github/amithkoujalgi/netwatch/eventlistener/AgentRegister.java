package io.github.amithkoujalgi.netwatch.eventlistener;


import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Getter
@Component
@Scope("singleton")
public class AgentRegister {

  private int count = 0;

  public void incrementCount() {
    count++;
  }
}
