package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.persistence.entity.tournament.CanchaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanchaRepository extends JpaRepository<CanchaEntity, UUID> {
    List<CanchaEntity> findByTournamentId(UUID tournamentId);
}
