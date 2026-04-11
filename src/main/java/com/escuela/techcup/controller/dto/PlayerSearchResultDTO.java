package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;

/**
 * DTO de respuesta con la info mínima de un jugador (RF-15).
 */
public class PlayerSearchResultDTO {

    private String playerId;
    private String name;
    private String mail;
    private Position position;
    private int dorsalNumber;
    private Gender gender;
    private PlayerStatus status;
    private String teamName;    // null si no tiene equipo

    public PlayerSearchResultDTO() {}

    public PlayerSearchResultDTO(String playerId, String name, String mail,
                                 Position position, int dorsalNumber,
                                 Gender gender, PlayerStatus status, String teamName) {
        this.playerId = playerId;
        this.name = name;
        this.mail = mail;
        this.position = position;
        this.dorsalNumber = dorsalNumber;
        this.gender = gender;
        this.status = status;
        this.teamName = teamName;
    }

    public String getPlayerId()       { return playerId; }
    public String getName()           { return name; }
    public String getMail()           { return mail; }
    public Position getPosition()     { return position; }
    public int getDorsalNumber()      { return dorsalNumber; }
    public Gender getGender()         { return gender; }
    public PlayerStatus getStatus()   { return status; }
    public String getTeamName()       { return teamName; }
}