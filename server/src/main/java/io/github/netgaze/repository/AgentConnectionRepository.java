package io.github.netgaze.repository;


import io.github.netgaze.models.db.AgentConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AgentConnectionRepository extends JpaRepository<AgentConnectionEntity, Long> {
    List<AgentConnectionEntity> findByAgent_Id(int agentId);

    List<AgentConnectionEntity> findByAgent_IdAndConnection_Id(int agentId, int connectionId);
}