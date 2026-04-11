package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;

/**
 * DTO con los filtros opcionales para buscar jugadores (RF-15).
 * Todos los campos son opcionales — null significa "sin filtro".
 */
public class PlayerSearchDTO {

    private Position position;
    private Integer semester;
    private Integer age;
    private Gender gender;
    private String name;
    private String playerId;

    public PlayerSearchDTO() {}

    public Position getPosition() { return position; }
    public void setPosition(Position position) { this.position = position; }

    public Integer getSemester() { return semester; }
    public void setSemester(Integer semester) { this.semester = semester; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
}