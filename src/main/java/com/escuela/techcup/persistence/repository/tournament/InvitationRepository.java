package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.persistence.entity.tournament.InvitationEntity;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationEntity, UUID> {
    boolean existsByTeamIdAndPlayerId(UUID teamId, UUID playerId);
    List<InvitationEntity> findByPlayerId(UUID playerId);
    Optional<InvitationEntity> findByIdAndStatus(UUID id, InvitationStatus status);
}