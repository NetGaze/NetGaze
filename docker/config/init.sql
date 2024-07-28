-- Create database
CREATE DATABASE netgaze;

-- Switch to the netgaze database
\c netgaze

-- Create tables
CREATE TABLE agents
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(50),
    host         VARCHAR(50),
    last_seen_at TIMESTAMP
);

CREATE TABLE connections
(
    id              SERIAL PRIMARY KEY,
    name            VARCHAR(50),
    description     VARCHAR(50),
    connection_type VARCHAR(50),
    host            VARCHAR(50),
    port            VARCHAR(50),
    active          BOOLEAN,
    last_checked_at TIMESTAMP
);

CREATE TABLE agent_connections
(
    id            SERIAL PRIMARY KEY,
    agent_id      INT,
    connection_id INT,
    FOREIGN KEY (agent_id) REFERENCES agents (id),
    FOREIGN KEY (connection_id) REFERENCES connections (id)
);

-- Insert dummy entries into agents
INSERT INTO agents (id, name, host, last_seen_at)
VALUES (1, 'agent1', '192.168.0.1', '2021-01-01 00:00:00'),
       (2, 'agent2', '192.168.0.2', '2022-02-01 12:00:00');

-- Insert dummy entries into connections
INSERT INTO connections (id, name, description, connection_type, host, port, active, last_checked_at)
VALUES (1, 'conn1', 'Connection to server 1 - port1', 'http', 'google.com', '8080', TRUE, '2024-01-01 10:00:00'),
       (2, 'conn2', 'Connection to server 1 - port2', 'tcp', '192.168.1.1', '5050', FALSE, '2024-02-01 11:00:00'),
       (3, 'conn3', 'Connection to server 2 - port1', 'http', '192.168.1.2', '6060', TRUE, '2024-03-01 12:00:00'),
       (4, 'conn4', 'Connection to server 2 - port2', 'tcp', '192.168.1.3', '7070', TRUE, '2024-03-01 12:00:00');

-- Insert dummy entries into agent_connections
INSERT INTO agent_connections (agent_id, connection_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4);