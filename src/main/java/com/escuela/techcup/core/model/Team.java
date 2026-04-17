package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import java.util.List;

import com.escuela.techcup.core.model.enums.Formation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Team {
    private String id;
    private String name;
    @JsonIgnore
    private BufferedImage logo;
    private String uniformColor;
    private Formation formation;
    private Captain captain;
    private Payment payment;
    private Tournament tournament;
    private List<Player> players;

    public Team(String id, String name, String uniformColor, BufferedImage logo, Formation formation) {
        this.id = id;
        this.name = name;
        this.uniformColor = uniformColor;
        this.logo = logo;
        this.formation = formation;
    }
}