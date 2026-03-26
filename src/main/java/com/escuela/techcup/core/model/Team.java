package com.escuela.techcup.core.model;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import com.escuela.techcup.core.model.enums.Formation;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Team {
    private String id;
    private String name;
    private BufferedImage logo;
    private List<Color> uniformColors;
    private Formation formation;

    private Captain captain;
    private List<Player> players;

    private Payment payment;

}
