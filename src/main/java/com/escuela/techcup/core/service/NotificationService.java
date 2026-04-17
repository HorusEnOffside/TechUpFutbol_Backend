package com.escuela.techcup.core.service;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.core.model.Notification;

import java.util.List;

public interface NotificationService {

    boolean hasNotifications(Long userId);
    List<Notification> getNotifications(Long userId);
    Notification createNotification(Notification model);
}