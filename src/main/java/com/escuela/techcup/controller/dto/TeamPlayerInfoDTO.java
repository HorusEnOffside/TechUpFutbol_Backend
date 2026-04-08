package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamPlayerInfoDTO {
    private String playerId;
    private String name;
    private String mail;
    private Position position;
    private int dorsalNumber;
    private boolean isCaptain;
}