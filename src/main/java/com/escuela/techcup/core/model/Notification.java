package com.escuela.techcup.core.model;

import com.escuela.techcup.core.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private UUID id;
    private UUID userId;
    private NotificationType type;
    private String title;
    private String description;
    private UUID relatedId;
    private LocalDateTime dateTime;
    private boolean read;
}
