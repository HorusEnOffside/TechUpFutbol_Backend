package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    public Notification toModel(NotificationEntity entity) {
        return Notification.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .sender(entity.getSender())
                .description(entity.getDescription())
                .dateTime(entity.getDateTime())
                .read(entity.isRead())
                .build();
    }

    public NotificationEntity toEntity(Notification model) {
        return NotificationEntity.builder()
                .id(model.getId())
                .userId(model.getUserId())
                .sender(model.getSender())
                .description(model.getDescription())
                .dateTime(model.getDateTime())
                .read(model.isRead())
                .build();
    }

    public Notification toModel(NotificationDTO dto) {
        return Notification.builder()
                .userId(dto.getUserId())
                .sender(dto.getSender())
                .description(dto.getDescription())
                .dateTime(dto.getDateTime())
                .read(false)
                .build();
    }

    public NotificationResponseDTO toResponseDTO(Notification model) {
        return NotificationResponseDTO.builder()
                .id(model.getId())
                .sender(model.getSender())
                .description(model.getDescription())
                .dateTime(model.getDateTime())
                .read(model.isRead())
                .build();
    }

    public List<Notification> toModelList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> toResponseDTOList(List<Notification> models) {
        return models.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}