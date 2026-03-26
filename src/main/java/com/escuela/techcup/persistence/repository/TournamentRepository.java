package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TournamentRepository extends JpaRepository<TournamentEntity, UUID> {

    List<TournamentEntity> findByStatus(TournamentStatus status);

    List<TournamentEntity> findByOrganizerId(UUID organizerId);
}