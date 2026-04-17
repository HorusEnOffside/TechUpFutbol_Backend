package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.NotificationNotFoundException;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import com.escuela.techcup.persistence.repository.tournament.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private NotificationEntity entity;
    private Notification model;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2024, 4, 16, 10, 0);

        entity = NotificationEntity.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime).read(false)
                .build();

        model = Notification.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime).read(false)
                .build();
    }


    @Test
    void shouldReturnTrueWhenUserHasUnreadNotifications() {
        when(notificationRepository.existsByUserIdAndReadFalse(10L)).thenReturn(true);

        boolean result = notificationService.hasNotifications(10L);

        assertTrue(result);
        verify(notificationRepository).existsByUserIdAndReadFalse(10L);
    }

    @Test
    void shouldReturnFalseWhenUserHasNoUnreadNotifications() {
        when(notificationRepository.existsByUserIdAndReadFalse(10L)).thenReturn(false);

        boolean result = notificationService.hasNotifications(10L);

        assertFalse(result);
        verify(notificationRepository).existsByUserIdAndReadFalse(10L);
    }

    @Test
    void shouldReturnFalseForNonExistentUser() {
        when(notificationRepository.existsByUserIdAndReadFalse(999L)).thenReturn(false);

        boolean result = notificationService.hasNotifications(999L);

        assertFalse(result);
    }

    @Test
    void shouldReturnNotificationsForValidUser() {
        when(notificationRepository.findByUserIdAndReadFalse(10L)).thenReturn(List.of(entity));
        when(notificationMapper.toModelList(List.of(entity))).thenReturn(List.of(model));

        List<Notification> result = notificationService.getNotifications(10L);

        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getSender());
        verify(notificationRepository).findByUserIdAndReadFalse(10L);
        verify(notificationMapper).toModelList(List.of(entity));
    }

    @Test
    void shouldReturnMultipleNotificationsForUser() {
        NotificationEntity entity2 = NotificationEntity.builder()
                .id(2L).userId(10L).sender("System").build();
        Notification model2 = Notification.builder()
                .id(2L).userId(10L).sender("System").build();

        when(notificationRepository.findByUserIdAndReadFalse(10L))
                .thenReturn(List.of(entity, entity2));
        when(notificationMapper.toModelList(List.of(entity, entity2)))
                .thenReturn(List.of(model, model2));

        List<Notification> result = notificationService.getNotifications(10L);

        assertEquals(2, result.size());
    }

    @Test
    void shouldThrowNotificationNotFoundExceptionWhenNoNotifications() {
        when(notificationRepository.findByUserIdAndReadFalse(10L))
                .thenReturn(Collections.emptyList());

        NotificationNotFoundException exception = assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getNotifications(10L)
        );

        assertTrue(exception.getMessage().contains("10"));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void shouldThrowExceptionForNonExistentUser() {
        when(notificationRepository.findByUserIdAndReadFalse(999L))
                .thenReturn(Collections.emptyList());

        assertThrows(
                NotificationNotFoundException.class,
                () -> notificationService.getNotifications(999L)
        );
    }

    @Test
    void shouldNotCallMapperWhenRepositoryReturnsEmpty() {
        when(notificationRepository.findByUserIdAndReadFalse(10L))
                .thenReturn(Collections.emptyList());

        assertThrows(NotificationNotFoundException.class,
                () -> notificationService.getNotifications(10L));

        verify(notificationMapper, never()).toModelList(any());
    }


    @Test
    void shouldCreateAndReturnNotification() {
        NotificationEntity savedEntity = NotificationEntity.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime).read(false)
                .build();

        when(notificationMapper.toEntity(model)).thenReturn(entity);
        when(notificationRepository.save(entity)).thenReturn(savedEntity);
        when(notificationMapper.toModel(savedEntity)).thenReturn(model);

        Notification result = notificationService.createNotification(model);

        assertNotNull(result);
        assertEquals("Admin", result.getSender());
        assertEquals(1L, result.getId());
        verify(notificationRepository).save(entity);
    }

    @Test
    void shouldPersistCreatedNotificationWithReadFalse() {
        when(notificationMapper.toEntity(model)).thenReturn(entity);
        when(notificationRepository.save(entity)).thenReturn(entity);
        when(notificationMapper.toModel(entity)).thenReturn(model);

        Notification result = notificationService.createNotification(model);

        assertFalse(result.isRead());
    }

    @Test
    void shouldCallRepositorySaveOnce() {
        when(notificationMapper.toEntity(model)).thenReturn(entity);
        when(notificationRepository.save(entity)).thenReturn(entity);
        when(notificationMapper.toModel(entity)).thenReturn(model);

        notificationService.createNotification(model);

        verify(notificationRepository, times(1)).save(any());
    }

    @Test
    void shouldCallMapperToEntityBeforeSaving() {
        when(notificationMapper.toEntity(model)).thenReturn(entity);
        when(notificationRepository.save(entity)).thenReturn(entity);
        when(notificationMapper.toModel(entity)).thenReturn(model);

        notificationService.createNotification(model);

        verify(notificationMapper).toEntity(model);
        verify(notificationRepository).save(entity);
        verify(notificationMapper).toModel(entity);
    }
}
