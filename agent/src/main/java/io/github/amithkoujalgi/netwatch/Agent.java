package io.github.amithkoujalgi.netwatch;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {

  private String name;
  private List<Connection> connections;
}