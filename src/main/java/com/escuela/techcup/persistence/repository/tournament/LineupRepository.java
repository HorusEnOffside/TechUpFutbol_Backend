package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.persistence.entity.tournament.LineupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineupRepository extends JpaRepository<LineupEntity, UUID> {
    Optional<LineupEntity> findByMatchIdAndTeamId(UUID matchId, UUID teamId);
    boolean existsByMatchIdAndTeamId(UUID matchId, UUID teamId);
}
