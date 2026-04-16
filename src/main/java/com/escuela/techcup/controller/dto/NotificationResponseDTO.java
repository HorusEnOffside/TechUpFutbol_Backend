package com.escuela.techcup.controller.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDTO {
    private Long id;
    private String sender;
    private String description;
    private LocalDateTime dateTime;
    private boolean read;
}