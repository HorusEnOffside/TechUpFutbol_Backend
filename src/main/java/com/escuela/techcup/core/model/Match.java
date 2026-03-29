package com.escuela.techcup.core.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class Match {
    private String id;
    private LocalDateTime dateTime;
    private Team teamA;
    private Team teamB;

    private Referee referee;

    private SoccerField soccerField;

    private List<MatchEvent> events;
}
