package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    boolean hasNotifications(UUID userId);
    List<Notification> getUnreadNotifications(UUID userId);
    List<Notification> getAllNotifications(UUID userId);
    Notification createNotification(Notification model);
    void markAsRead(UUID notificationId);
}
