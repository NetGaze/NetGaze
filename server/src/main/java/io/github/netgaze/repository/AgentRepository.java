package io.github.netgaze.repository;


import io.github.netgaze.models.db.AgentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgentRepository extends JpaRepository<AgentEntity, Long> {
    List<AgentEntity> findByNameAndHost(String name, String host);
}