package io.github.netgaze.models.db;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "agents")
@Data
public class AgentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "host")
    private String host;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AgentConnectionEntity> agentConnections;

    @Transient
    public Set<ConnectionEntity> getConnections() {
        return agentConnections.stream()
                .map(AgentConnectionEntity::getConnection)
                .collect(Collectors.toSet());
    }
}
