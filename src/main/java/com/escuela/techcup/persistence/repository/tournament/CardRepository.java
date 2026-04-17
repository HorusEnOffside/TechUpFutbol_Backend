package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.persistence.entity.tournament.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByPlayer_Id(UUID playerId);
    List<CardEntity> findByPlayer_Team_Id(UUID teamId);
    List<CardEntity> findByPlayer_Team_Tournament_Id(UUID tournamentId);
    List<CardEntity> findByPlayer_Team_Tournament_IdAndPlayer_Id(UUID tournamentId, UUID playerId);
    List<CardEntity> findByPlayer_Team_Tournament_IdAndPlayer_Team_Id(UUID tournamentId, UUID teamId);
}

