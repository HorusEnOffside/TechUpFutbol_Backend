package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.HorarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioRepository extends JpaRepository<HorarioEntity, String> {
    List<HorarioEntity> findByTournamentId(String tournamentId);
}
