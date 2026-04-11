package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Position;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GraduatePlayerDTO extends GraduateUserDTO {

    @Min(value = 1, message = "Dorsal number must be greater than 0")
    private int dorsalNumber;

    @NotNull(message = "position is required")
    private Position position;
}