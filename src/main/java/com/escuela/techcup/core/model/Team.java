package com.escuela.techcup.core.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

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

}
