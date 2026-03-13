package com.escuela.techcup.model;

import com.escuela.techcup.model.enums.Position;

import lombok.Data;

import com.escuela.techcup.model.enums.PlayerStatus;

@Data
public class Player implements ComponentPlayer {
    private final UserPlayer userPlayer;
    private Position position;
    private int dorsalNumber;
    private PlayerStatus status;
    private Team team;
    
    public Player(UserPlayer userPlayer, Position position, int dorsalNumber) {
        this.userPlayer = userPlayer;
        this.position = position;
        this.dorsalNumber = dorsalNumber;
        this.status = PlayerStatus.DISPONIBLE;
    }

    public Player(UserPlayer userPlayer, Position position, int dorsalNumber, Team team) {
        this.userPlayer = userPlayer;
        this.position = position;
        this.dorsalNumber = dorsalNumber;
        this.status = PlayerStatus.EN_EQUIPO;
        this.team = team;
    }
}
