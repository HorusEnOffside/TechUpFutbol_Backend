package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Boolean> hasNotifications(@PathVariable Long userId) {
        log.info("Request to check notifications for userId={}", userId);
        return ResponseEntity.ok(notificationService.hasNotifications(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(@PathVariable Long userId) {
        log.info("Request to get notifications for userId={}", userId);
        List<Notification> models = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notificationMapper.toResponseDTOList(models));
    }

    @PreAuthorize("hasAnyRole('ORGANIZER', 'ADMIN')")
    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@Valid @RequestBody NotificationDTO requestDTO) {
        log.info("Request to create notification");
        Notification model = notificationMapper.toModel(requestDTO);
        Notification created = notificationService.createNotification(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationMapper.toResponseDTO(created));
    }
}