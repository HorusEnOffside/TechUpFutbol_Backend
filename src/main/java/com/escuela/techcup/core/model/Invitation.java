package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.InvitationStatus;
import lombok.Data;

@Data
public class Invitation {
    private String id;
    private String teamId;
    private String teamName;
    private String playerId;
    private String message;
    private InvitationStatus status;
}