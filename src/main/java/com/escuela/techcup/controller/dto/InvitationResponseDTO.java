package com.escuela.techcup.controller.dto;

import com.escuela.techcup.core.model.enums.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvitationResponseDTO {
    private String id;
    private String teamId;
    private String teamName;
    private String message;
    private InvitationStatus status;
}