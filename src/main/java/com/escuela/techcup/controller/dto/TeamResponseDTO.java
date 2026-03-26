package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamResponseDTO {
    private String name;
    private Formation formation;
    private List<Player> players;
    private List<Color> uniformColors;

    public TeamResponseDTO(String name, Formation formation, List<Player> players, List<Color> uniformColors) {
        this.name = name;
        this.formation = formation;
        this.players = new ArrayList<>();
        this.uniformColors = new ArrayList<>();


    }
}
