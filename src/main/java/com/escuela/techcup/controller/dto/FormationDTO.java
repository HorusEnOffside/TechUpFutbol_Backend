package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Formation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormationDTO {
    @NotBlank
    private String matchId;

    @NotNull
    private Formation formation;
}
