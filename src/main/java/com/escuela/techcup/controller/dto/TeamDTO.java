package com.escuela.techcup.controller.dto;


import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.Formation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.awt.*;
import java.util.List;

@Data
@NoArgsConstructor
public class TeamDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;


    @NotEmpty(message = "Debe incluir al menos un color")
    private List<Color> uniformColors;

    @NotNull(message = "La formación es obligatoria")
    private Formation formation;


    private List<Player> players;

    public TeamDTO(String name, List<Color> uniformColors, Formation formation, List<Player> players) {
        this.name = name;
        this.formation = formation;
        this.uniformColors = uniformColors;
        this.players = players;
    }
}
