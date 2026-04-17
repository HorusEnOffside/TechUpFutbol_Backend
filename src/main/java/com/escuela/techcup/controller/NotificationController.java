package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notificaciones", description = "Gestión de notificaciones de usuarios")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/has/{userId}")
    public ResponseEntity<Boolean> hasNotifications(@PathVariable UUID userId) {
        log.info("Check unread notifications for userId={}", userId);
        return ResponseEntity.ok(notificationService.hasNotifications(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications(@PathVariable UUID userId) {
        log.info("Get all notifications for userId={}", userId);
        List<Notification> models = notificationService.getAllNotifications(userId);
        return ResponseEntity.ok(notificationMapper.toResponseDTOList(models));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getUnreadNotifications(@PathVariable UUID userId) {
        log.info("Get unread notifications for userId={}", userId);
        List<Notification> models = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notificationMapper.toResponseDTOList(models));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID id) {
        log.info("Mark notification as read id={}", id);
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationDTO requestDTO) {
        log.info("Manual notification creation for userId={}", requestDTO.getUserId());
        Notification model = notificationMapper.toModel(requestDTO);
        Notification created = notificationService.createNotification(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationMapper.toResponseDTO(created));
    }
}
