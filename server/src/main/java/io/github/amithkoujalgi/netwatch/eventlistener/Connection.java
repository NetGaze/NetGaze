package io.github.amithkoujalgi.netwatch.eventlistener;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

  private String name;
  private String type;
  private String host;
  private int port;
  private boolean active;
  private Date lastCheckedAt;
}