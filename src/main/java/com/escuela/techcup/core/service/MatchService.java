package com.escuela.techcup.core.service;

import java.util.List;
import java.time.LocalDate;

import com.escuela.techcup.controller.dto.MatchResultDTO;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.persistence.entity.tournament.CardEntity;

public interface MatchService {

    Match getMatchById(String id);
    List<Match> getAllMatches();
    List<Match> getMatchesByRefereeId(String refereeId);
    Match createMatch(LocalDate date, String teamAId, String teamBId);
    Match setReferee(String matchId, String refereeId);
    Match setSoccerField(String matchId, String soccerFieldId);
    Match addMatchEventGoal(String matchId, String playerId, int minute, String description);
    Match addMatchEventCard(String matchId, String playerId, int minute, CardEntity.CardType type, String description);
    Match finalizeMatch(String matchId, MatchResultDTO result);

}
