package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;

import lombok.Data;

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

    public String getUserId() {
        return userPlayer.getId();
    }
    
    public String getName() {
        return userPlayer.getName();
    }

    public String getMail() {
        return userPlayer.getMail();
    }

    public BufferedImage getProfilePicture() {
        return userPlayer.getProfilePicture();
    }

    public LocalDate getDateOfBirth() {
        return userPlayer.getDateOfBirth();
    }

    public Gender getGender() {
        return userPlayer.getGender();
    }

    public String getPassword() {
        return userPlayer.getPassword();
    }
}
