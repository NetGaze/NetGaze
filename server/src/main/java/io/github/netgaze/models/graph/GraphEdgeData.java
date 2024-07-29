package io.github.netgaze.models.graph;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GraphEdgeData {

  private String id;
  private String source;
  private String target;

}
