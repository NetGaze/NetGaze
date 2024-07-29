package io.github.netgaze.service.impl;

import io.github.netgaze.Agent;
import io.github.netgaze.Connection;
import io.github.netgaze.models.db.AgentConnectionEntity;
import io.github.netgaze.models.db.AgentEntity;
import io.github.netgaze.models.db.ConnectionEntity;
import io.github.netgaze.repository.AgentConnectionRepository;
import io.github.netgaze.repository.AgentRepository;
import io.github.netgaze.repository.ConnectionRepository;
import io.github.netgaze.service.AgentService;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class AgentServiceImpl implements AgentService {
    private final AgentRepository agentRepository;

    private final ConnectionRepository connectionRepository;
    private final AgentConnectionRepository agentConnectionRepository;

    public AgentServiceImpl(AgentRepository agentRepository, ConnectionRepository connectionRepository, AgentConnectionRepository agentConnectionRepository) {
        this.agentRepository = agentRepository;
        this.connectionRepository = connectionRepository;
        this.agentConnectionRepository = agentConnectionRepository;
    }

    @Override
    public void addOrUpdateAgent(Agent agent) {
        AgentEntity agentEntity = null;
        List<AgentEntity> agents = agentRepository.findByNameAndHost(agent.getName(), agent.getHost());
        if (agents.isEmpty()) {
            agentEntity = new AgentEntity();
            agentEntity.setName(agent.getName());
            agentEntity.setHost(agent.getHost());
            agentRepository.save(agentEntity);
        } else {
            agentEntity = agents.get(0);
        }
        for (Connection connection : agent.getConnections()) {
            List<ConnectionEntity> connectionEntities = connectionRepository.findByNameAndHostAndPortAndConnectionType(connection.getName(), connection.getHost(), connection.getPort(), connection.getType().name());
            ConnectionEntity connectionEntity = null;
            if (connectionEntities.isEmpty()) {
                connectionEntity = new ConnectionEntity();
                connectionEntity.setName(connection.getName());
                connectionEntity.setHost(connection.getHost());
                connectionEntity.setPort(connection.getPort());
                connectionEntity.setConnectionType(String.valueOf(connection.getType()));
                connectionEntity.setDescription(connection.getDescription());
            } else {
                connectionEntity = connectionEntities.get(0);
            }
            connectionEntity.setIsActive(connection.isActive());
            connectionEntity.setLastCheckedAt(connection.getLastCheckedAt().atZone(ZoneOffset.UTC).toLocalDateTime());
            connectionRepository.save(connectionEntity);
            List<AgentConnectionEntity> agentConnectionEntities = agentConnectionRepository.findByAgent_IdAndConnection_Id(agentEntity.getId(), connectionEntity.getId());
            AgentConnectionEntity agentConnectionEntity = null;
            if (agentConnectionEntities.isEmpty()) {
                agentConnectionEntity = new AgentConnectionEntity();
            } else {
                agentConnectionEntity = agentConnectionEntities.get(0);
            }
            agentConnectionEntity.setAgent(agentEntity);
            agentConnectionEntity.setConnection(connectionEntity);
            agentConnectionRepository.save(agentConnectionEntity);
        }
    }

    @Override
    public List<Agent> listAgents() {
        List<Agent> agents = new ArrayList<>();
        List<AgentEntity> agentEntities = agentRepository.findAll();
        agentEntities.forEach(agentEntity -> {
            Agent agent = new Agent();
            agent.setName(agentEntity.getName());
            agent.setHost(agentEntity.getHost());

            agentConnectionRepository.findByAgent_Id(agentEntity.getId()).forEach(agentConnectionEntity -> {
                Connection connection = new Connection();
                ConnectionEntity connectionEntity = agentConnectionEntity.getConnection();
                connection.setName(connectionEntity.getName());
                connection.setHost(connectionEntity.getHost());
                connection.setPort(connectionEntity.getPort());
                agent.getConnections().add(connection);
            });
            agents.add(agent);
        });
        return agents;
    }
}

