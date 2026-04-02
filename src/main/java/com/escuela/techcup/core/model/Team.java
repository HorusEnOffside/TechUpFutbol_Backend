package com.escuela.techcup.core.model;

import java.awt.image.BufferedImage;
import com.escuela.techcup.core.model.enums.Formation;
import lombok.Data;

@Data
public class Team {
    private String id;
    private String name;
    private BufferedImage logo;
    private String uniformColor;
    private Formation formation;
    private Captain captain;
    private Payment payment;

    public Team(String id, String name, String uniformColor, BufferedImage logo) {
        this.id = id;
        this.name = name;
        this.uniformColor = uniformColor;
        this.logo = logo;
    }
}