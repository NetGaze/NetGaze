package io.github.netgaze.models.graph;

import java.util.HashMap;
import java.util.Map;
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
public class GraphNode {

  private GraphNodeData data;
  private GraphNodePosition position;
  private Map<String, Object> additionalProperties = new HashMap<>();
  private String classes;
}
