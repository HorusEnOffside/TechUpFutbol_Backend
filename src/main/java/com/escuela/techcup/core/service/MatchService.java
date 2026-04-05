package com.escuela.techcup.core.service;

import java.util.List;
import java.time.LocalDate;

import com.escuela.techcup.core.model.Match;

public interface MatchService {

    Match getMatchById(String id);
    List<Match> getAllMatches();
    Match createMatch(LocalDate date, String teamAId, String teamBId);
    Match setReferee(String matchId, String refereeId);
    Match setSoccerField(String matchId, String soccerFieldId);
    Match addMatchEventGoal(String matchId, String playerId, int minute,  String description);
    
}
