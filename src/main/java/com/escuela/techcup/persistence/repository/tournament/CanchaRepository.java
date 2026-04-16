package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.CanchaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanchaRepository extends JpaRepository<CanchaEntity, String> {
    List<CanchaEntity> findByTournamentId(String tournamentId);
}
