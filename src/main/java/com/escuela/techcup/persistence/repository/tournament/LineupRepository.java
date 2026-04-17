package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.LineupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineupRepository extends JpaRepository<LineupEntity, String> {
    Optional<LineupEntity> findByMatchIdAndTeamId(String matchId, String teamId);
    boolean existsByMatchIdAndTeamId(String matchId, String teamId);
}
