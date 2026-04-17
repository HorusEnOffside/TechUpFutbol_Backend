package com.escuela.techcup.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {
    private Long id;
    private Long userId;
    private String sender;
    private String description;
    private LocalDateTime dateTime;
    private boolean read;
}