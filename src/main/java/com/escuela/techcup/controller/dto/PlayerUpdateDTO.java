package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Position;
import lombok.Data;

@Data
public class PlayerUpdateDTO {
    private Position position;
    private Integer dorsalNumber;
    private Integer semester;
}
