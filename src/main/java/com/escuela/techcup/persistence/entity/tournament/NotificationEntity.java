package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class NotificationEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private Long id;

    private Long userId;
    private String sender;
    private String description;
    private LocalDateTime dateTime;
    private boolean read = false;
}
