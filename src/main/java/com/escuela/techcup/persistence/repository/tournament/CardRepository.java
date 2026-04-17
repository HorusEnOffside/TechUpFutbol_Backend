package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    List<CardEntity> findByPlayer_Id(String playerId);
    List<CardEntity> findByPlayer_Team_Id(String teamId);
    List<CardEntity> findByPlayer_Team_Tournament_Id(String tournamentId);
    List<CardEntity> findByPlayer_Team_Tournament_IdAndPlayer_Id(String tournamentId, String playerId);
    List<CardEntity> findByPlayer_Team_Tournament_IdAndPlayer_Team_Id(String tournamentId, String teamId);
}

