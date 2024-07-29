package io.github.netgaze;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Agent {

  private String name;
  private String host;
  private Date lastSeenAt;
  private List<Connection> connections = new ArrayList<>();
}
