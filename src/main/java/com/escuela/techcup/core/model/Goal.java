package com.escuela.techcup.core.model;

import lombok.Data;

@Data
public class Goal implements MatchEvent {
    private final String id;
    private final int minute;
    private final ComponentPlayer player;
    private final String description;

    public Goal(String id, int minute, ComponentPlayer playerName, String description) {
        this.id = id;
        this.minute = minute;
        this.player = playerName;
        this.description = description;
    }
}
