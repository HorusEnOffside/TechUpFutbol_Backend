package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.MatchEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, String> {

    List<MatchEntity> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<MatchEntity> findByRefereeId(String refereeId);

    List<MatchEntity> findBySoccerFieldId(String soccerFieldId);

}