package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.InvitationEntity;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<InvitationEntity, String> {
    boolean existsByTeamIdAndPlayerId(String teamId, String playerId);
    List<InvitationEntity> findByPlayerId(String playerId);
    Optional<InvitationEntity> findByIdAndStatus(String id, InvitationStatus status);
}