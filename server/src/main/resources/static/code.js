// noinspection JSUnresolvedReference,JSJQueryEfficiency

$(document).ready(function () {
    $('body').append('<div id="highlighter" style="border: 1px black solid; display: inline-block;"></div>');
});

function convertToReadable(variableName) {
    return variableName
        .replace(/([a-z])([A-Z])/g, '$1 $2') // Add space between lowercase and uppercase letters
        .replace(/_/g, ' ') // Replace underscores with spaces
        .replace(/\b\w/g, c => c.toUpperCase()); // Capitalize first letter of each word
}

function render(graphData) {
    let cy = window.cy = cytoscape({
        container: document.getElementById('cy'),
        boxSelectionEnabled: false,
        userZoomingEnabled: false,
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
            },
            {
                selector: '.node-active',
                css: {
                    'background-color': 'green'
                }
            },
            {
                selector: '.node-inactive',
                css: {
                    'background-color': 'red'
                }
            },
            {
                selector: '.agent',
                css: {
                    'shape': 'rectangle',
                }
            },
            {
                selector: '.connection',
                css: {
                    'shape': 'ellipse',
                }
            }
        ],
        elements: graphData,
        layout: {
            name: 'circle',
            padding: 5
        }
    });

    cy.nodes().forEach(node => {
        // console.log(graphData);
        for (g in graphData.nodes) {
            if (node.id() === graphData.nodes[g].data.id) {
                node.data('additionalProperties', graphData.nodes[g].additionalProperties);
            }
        }
    });

    cy.on('mouseover', 'node', function (evt) {
        let node = evt.target;
        let nodeId = node.id();
        let nodePosition = node.position();
        let nodeAdditionalProperties = node.data().additionalProperties;
        // evt.target.style({
        //     'background-color': 'yellow'
        // });
        // console.log(node.css().shape);
        let text = "";
        if (node.css().shape === 'ellipse') {
            nodeAdditionalProperties['Connection'] = node.id();
        } else if (node.css().shape === 'rectangle') {
            nodeAdditionalProperties['Agent'] = node.id();
        }
        nodeAdditionalProperties['Connection'] = node.id();
        for (const key in nodeAdditionalProperties) {
            if (nodeAdditionalProperties.hasOwnProperty(key)) {
                let value = nodeAdditionalProperties[key];
                let keyReadable = convertToReadable(key);
                text = text + `<b>${keyReadable}</b>: ${value}<br/>`;
            }
        }
        $('#highlighter').html(text);

        $('#highlighter').css({
            // left: (node.position().x + 50) + "px",
            // top: (node.position().y + 50) + "px",
            left: '90%',
            top: '0%',
            position: 'fixed'
        });
        $('#highlighter').show();
    });

    cy.on('mouseout', 'node', function (evt) {
        let node = evt.target;
        // evt.target.style({
        //     'background-color': 'gray'
        // });
        $('#highlighter').hide();
    });
    // window.cy.nodes().map(x => console.log(x.json()));
}


async function fetchGraphData() {
    const response = await fetch("http://localhost:8080/graph", {
        headers: {
            accept: "application/json",
        },
        method: "GET",
        mode: "cors",
    });
    return await response.json();
}

function getGraph() {
    fetchGraphData().then((graph) => {
        render(graph);
    }).catch((error) => {
        console.error(error);
    });
}

setInterval(getGraph, 1000);

