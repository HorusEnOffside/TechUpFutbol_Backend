package com.escuela.techcup.model;

import java.time.LocalDateTime;

import com.escuela.techcup.model.enums.TournamentStatus;

public class Tournament {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int teamsAmount;
    private Double teamCost;
    private TournamentStatus status;

    private Organizer organizer;

}
