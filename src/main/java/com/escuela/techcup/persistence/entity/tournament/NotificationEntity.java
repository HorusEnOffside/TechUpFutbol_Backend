package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
