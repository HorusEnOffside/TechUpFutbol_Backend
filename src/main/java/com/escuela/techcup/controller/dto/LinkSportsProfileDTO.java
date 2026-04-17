package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.Position;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LinkSportsProfileDTO {

    @NotBlank(message = "userId is required")
    private String userId;

    @NotNull(message = "position is required")
    private Position position;

    @Min(value = 1, message = "dorsalNumber must be greater than 0")
    private int dorsalNumber;
}
