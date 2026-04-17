package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import com.escuela.techcup.persistence.repository.tournament.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public boolean hasNotifications(Long userId) {
        return notificationRepository.existsByUserIdAndReadFalse(userId);
    }

    @Override
    public List<Notification> getNotifications(Long userId) {
        List<NotificationEntity> entities = notificationRepository.findByUserIdAndReadFalse(userId);
        return notificationMapper.toModelList(entities);
    }

    @Override
    public Notification createNotification(Notification model) {
        NotificationEntity entity = notificationMapper.toEntity(model);
        NotificationEntity saved = notificationRepository.save(entity);
        return notificationMapper.toModel(saved);
    }
}