package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.NotificationNotFoundException;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import com.escuela.techcup.persistence.repository.tournament.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean hasNotifications(UUID userId) {
        return notificationRepository.existsByUserIdAndReadFalse(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(UUID userId) {
        List<NotificationEntity> entities =
                notificationRepository.findByUserIdAndReadFalseOrderByDateTimeDesc(userId);
        return notificationMapper.toModelList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getAllNotifications(UUID userId) {
        List<NotificationEntity> entities =
                notificationRepository.findByUserIdOrderByDateTimeDesc(userId);
        return notificationMapper.toModelList(entities);
    }

    @Override
    @Transactional
    public Notification createNotification(Notification model) {
        if (model.getId() == null) model.setId(UUID.randomUUID());
        NotificationEntity entity = notificationMapper.toEntity(model);
        return notificationMapper.toModel(notificationRepository.save(entity));
    }

    @Override
    @Transactional
    public void markAsRead(UUID notificationId) {
        NotificationEntity entity = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId.toString()));
        entity.setRead(true);
        notificationRepository.save(entity);
    }
}
