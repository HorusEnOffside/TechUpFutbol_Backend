package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {

    List<TournamentEntity> findByStatus(TournamentStatus status);

    List<TournamentEntity> findByOrganizerId(UUID organizerId);
}