package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.PlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    Optional<PlayerEntity> findByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    List<PlayerEntity> findByStatus(PlayerStatus status);

    List<PlayerEntity> findByPosition(Position position);
}