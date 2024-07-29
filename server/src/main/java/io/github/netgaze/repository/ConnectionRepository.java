package io.github.netgaze.repository;


import io.github.netgaze.models.db.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
    List<ConnectionEntity> findByNameAndHostAndPortAndConnectionType(String name, String host, Integer port, String connectionType);
}