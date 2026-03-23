package com.escuela.techcup.core.model;

import java.time.LocalDateTime;

import com.escuela.techcup.core.model.enums.TournamentStatus;

public class Tournament {
    private String id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int teamsAmount;
    private Double teamCost;
    private TournamentStatus status;

    private Organizer organizer;

}
