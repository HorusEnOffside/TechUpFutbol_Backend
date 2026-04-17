package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService; // ← interfaz, no la impl
    private final NotificationMapper notificationMapper;

    @GetMapping("/has/{userId}")
    public ResponseEntity<Boolean> hasNotifications(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.hasNotifications(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<NotificationResponseDTO>> getNotifications(@PathVariable Long userId) {
        List<Notification> models = notificationService.getNotifications(userId);
        return ResponseEntity.ok(notificationMapper.toResponseDTOList(models));
    }

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationDTO requestDTO) {
        Notification model = notificationMapper.toModel(requestDTO);
        Notification created = notificationService.createNotification(model);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationMapper.toResponseDTO(created));
    }
}