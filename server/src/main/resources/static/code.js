function render(graphData) {
    var cy = window.cy = cytoscape({
        container: document.getElementById('cy'),
        boxSelectionEnabled: false,
        style: [
            {
                selector: 'node',
                css: {
                    'shape': 'rectangle',
                    'content': 'data(id)',
                    'text-valign': 'center',
                    'text-halign': 'center',
                    'background-color': 'gray'
                }
            },
            {
                selector: ':parent',
                css: {
                    'text-valign': 'top',
                    'text-halign': 'center',
                    'shape': 'round-rectangle',
                    'corner-radius': "10",
                    'padding': 10
                }
            },
            {
                selector: 'node#e',
                css: {
                    'corner-radius': "10",
                    'padding': 0
                }
            },
            {
                selector: 'edge',
                css: {
                    'curve-style': 'bezier',
                    'target-arrow-shape': 'triangle'
                }
            }
        ],
        elements: graphData,
        layout: {
            name: 'circle',
            padding: 5
        }
    });
    cy.on('mouseover', 'node', function(evt){
      var node = evt.target;
      console.log( node.id() );
      evt.target.style({
          'background-color': 'yellow'
        });
    });
    cy.on('mouseout', 'node', function(evt){
      var node = evt.target;
      console.log( node.id() );
      evt.target.style({
          'background-color': 'gray'
        });
    });
}

async function fetchGraphData() {
    const response = await fetch("http://localhost:8080/graph", {
        headers: {
            accept: "application/json",
        },
        method: "GET",
        mode: "cors",
        credentials: "include",
    });
    const graph = await response.json();
    return graph;
}

function getGraph() {
    fetchGraphData().then((graph) => {
        console.log(graph);
        render(graph);
    }).catch((error) => {
        console.error(error);
    });
}

getGraph();
