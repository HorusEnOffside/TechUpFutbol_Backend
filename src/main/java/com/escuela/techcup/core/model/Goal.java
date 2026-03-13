package com.escuela.techcup.core.model;

public class Goal implements MatchEvent {
    private int minute;
    private ComponentPlayer player;

    public int getMinute() {
        return minute;
    }

    public ComponentPlayer getPlayer() {
        return player;
    }

}
