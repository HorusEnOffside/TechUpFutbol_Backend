package com.escuela.techcup.persistence.repository.users;

import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {

    Optional<PlayerEntity> findByUserId(String userId);

    boolean existsByUserId(String userId);

    List<PlayerEntity> findByStatus(PlayerStatus status);

    List<PlayerEntity> findByPosition(Position position);

    List<PlayerEntity> findByUser_NameContainingIgnoreCase(String name);
}