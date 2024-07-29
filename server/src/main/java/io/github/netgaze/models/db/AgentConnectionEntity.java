package io.github.netgaze.models.db;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "agent_connections")
@Data
public class AgentConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private AgentEntity agent;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    private ConnectionEntity connection;
}