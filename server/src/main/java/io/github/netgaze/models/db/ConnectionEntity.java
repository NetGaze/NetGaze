package io.github.netgaze.models.db;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "connections")
@Data
public class ConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "host")
    private String host;

    @Column(name = "port")
    private Integer port;

    @Column(name = "active")
    private Boolean isActive;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @OneToMany(mappedBy = "connection", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AgentConnectionEntity> agentConnections;

    @Transient
    public Set<AgentEntity> getAgents() {
        return agentConnections.stream()
                .map(AgentConnectionEntity::getAgent)
                .collect(Collectors.toSet());
    }
}
