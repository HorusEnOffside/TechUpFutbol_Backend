package com.escuela.techcup.core.model;

public class Card implements MatchEvent {
    public enum CardType { YELLOW, RED }

    private final String id;
    private final int minute;
    private final ComponentPlayer player;
    private final CardType type;
    private final String description;

    public Card(String id, int minute, ComponentPlayer player, CardType type, String description) {
        this.id = id;
        this.minute = minute;
        this.player = player;
        this.type = type;
        this.description = description;
    }

    @Override
    public int getMinute() { return minute; }

    @Override
    public ComponentPlayer getPlayer() { return player; }

    @Override
    public String getDescription() { return description; }

    public CardType getType() { return type; }
    public String getId() { return id; }
}

