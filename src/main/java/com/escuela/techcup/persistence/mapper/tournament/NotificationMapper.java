package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class NotificationMapper {

    public Notification toModel(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .relatedId(entity.getRelatedId())
                .dateTime(entity.getDateTime())
                .read(entity.isRead())
                .build();
    }

    public Notification toModel(NotificationDTO dto) {
        return Notification.builder()
                .id(UUID.randomUUID())
                .userId(dto.getUserId())
                .type(dto.getType())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .relatedId(dto.getRelatedId())
                .dateTime(LocalDateTime.now())
                .read(false)
                .build();
    }

    public NotificationEntity toEntity(Notification model) {
        return NotificationEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .type(model.getType())
                .title(model.getTitle())
                .description(model.getDescription())
                .relatedId(model.getRelatedId())
                .dateTime(model.getDateTime())
                .read(model.isRead())
                .build();
    }

    public NotificationResponseDTO toResponseDTO(Notification model) {
        return NotificationResponseDTO.builder()
                .id(model.getId())
                .type(model.getType())
                .title(model.getTitle())
                .description(model.getDescription())
                .relatedId(model.getRelatedId())
                .dateTime(model.getDateTime())
                .read(model.isRead())
                .build();
    }

    public List<NotificationResponseDTO> toResponseDTOList(List<Notification> models) {
        return models.stream().map(this::toResponseDTO).toList();
    }

    public List<Notification> toModelList(List<NotificationEntity> entities) {
        return entities.stream().map(this::toModel).toList();
    }
}
