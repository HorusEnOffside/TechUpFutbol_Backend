package com.escuela.techcup.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import com.escuela.techcup.model.enums.Formation;

public class Team {
    private String id;
    private String name;
    private BufferedImage logo;
    private List<Color> UniformColors;
    private Formation formation;

    private Captain captain;
    private List<Player> players;

    private Payment payment;

}
