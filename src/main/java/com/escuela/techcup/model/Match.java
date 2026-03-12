package com.escuela.techcup.model;

import java.sql.Ref;
import java.time.LocalDateTime;

public class Match {
    private String id;
    private LocalDateTime dateTime;
    private Team teamA;
    private Team teamB;

    private SoccerField soccerField;
    private Referee referee;
}
