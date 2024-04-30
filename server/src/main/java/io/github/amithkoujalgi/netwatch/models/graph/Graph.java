package io.github.amithkoujalgi.netwatch.models.graph;

import java.util.List;
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
public class Graph {

  private List<GraphNode> nodes;
  private List<GraphEdge> edges;

}
