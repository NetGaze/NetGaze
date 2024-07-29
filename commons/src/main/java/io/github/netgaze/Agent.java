package io.github.netgaze;

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
public class Agent {

  private String name;
  private String host;
  private Instant lastSeenAt;
  private List<Connection> connections = new ArrayList<>();
}
