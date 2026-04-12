package com.escuela.techcup.core.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Match {
    private final String id;
    private LocalDateTime dateTime;
    private Team teamA;
    private Team teamB;
    private Referee referee;
    private SoccerField soccerField;
    private List<MatchEvent> events;
    private String status; // "PENDING", "FINISHED"

    public Match(String id, LocalDateTime dateTime, Team teamA, Team teamB) {
        this.id = id;
        this.dateTime = dateTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.status = "PENDING";
    }

    public Match(String id, LocalDateTime dateTime, Team teamA, Team teamB, Referee referee, SoccerField soccerField) {
        this.id = id;
        this.dateTime = dateTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.referee = referee;
        this.soccerField = soccerField;
        this.status = "PENDING";
    }

    public Match(String id, LocalDateTime dateTime, Team teamA, Team teamB, Referee referee, SoccerField soccerField, List<MatchEvent> events, String status) {
        this.id = id;
        this.dateTime = dateTime;
        this.teamA = teamA;
        this.teamB = teamB;
        this.referee = referee;
        this.soccerField = soccerField;
        this.events = events;
        this.status = status;
    }
}
