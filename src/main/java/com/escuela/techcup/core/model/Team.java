package com.escuela.techcup.core.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import com.escuela.techcup.core.model.enums.Formation;
import lombok.Data;

@Data
public class Team {
    private String id;
    private String name;
    private BufferedImage logo;
    private Color uniformColor;
    private Formation formation;

    private Captain captain;

    private Payment payment;

    private Tournament tournament;

    private List<Player> players;

    public Team(String id, String name, BufferedImage logo, Color uniformColor, Formation formation) {
        this.id = id;
        this.name = name;
        this.logo = logo;
        this.uniformColor = uniformColor;
        this.formation = formation;

    }
}
