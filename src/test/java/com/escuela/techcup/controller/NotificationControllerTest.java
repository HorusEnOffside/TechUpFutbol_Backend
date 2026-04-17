package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.exception.NotificationNotFoundException;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationController notificationController;

    private Notification model;
    private NotificationResponseDTO responseDTO;
    private NotificationDTO requestDTO;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        fixedDateTime = LocalDateTime.of(2024, 4, 16, 10, 0);

        model = Notification.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime).read(false)
                .build();

        responseDTO = NotificationResponseDTO.builder()
                .id(1L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime).read(false)
                .build();

        requestDTO = NotificationDTO.builder()
                .userId(10L).sender("Admin")
                .description("Test notification")
                .dateTime(fixedDateTime)
                .build();
    }


    @Test
    void shouldReturn200TrueWhenUserHasNotifications() {
        when(notificationService.hasNotifications(10L)).thenReturn(true);

        ResponseEntity<Boolean> response = notificationController.hasNotifications(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void shouldReturn200FalseWhenUserHasNoNotifications() {
        when(notificationService.hasNotifications(10L)).thenReturn(false);

        ResponseEntity<Boolean> response = notificationController.hasNotifications(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void shouldReturn200FalseForNonExistentUser() {
        when(notificationService.hasNotifications(999L)).thenReturn(false);

        ResponseEntity<Boolean> response = notificationController.hasNotifications(999L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());
    }

    @Test
    void shouldCallServiceOnceForHasNotifications() {
        when(notificationService.hasNotifications(10L)).thenReturn(true);

        notificationController.hasNotifications(10L);

        verify(notificationService, times(1)).hasNotifications(10L);
    }

    @Test
    void shouldReturn200WithListOfNotifications() {
        when(notificationService.getNotifications(10L)).thenReturn(List.of(model));
        when(notificationMapper.toResponseDTOList(List.of(model))).thenReturn(List.of(responseDTO));

        ResponseEntity<List<NotificationResponseDTO>> response =
                notificationController.getNotifications(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Admin", response.getBody().get(0).getSender());
    }

    @Test
    void shouldReturn200WithMultipleNotifications() {
        Notification model2 = Notification.builder().id(2L).sender("System").build();
        NotificationResponseDTO dto2 = NotificationResponseDTO.builder()
                .id(2L).sender("System").build();

        when(notificationService.getNotifications(10L)).thenReturn(List.of(model, model2));
        when(notificationMapper.toResponseDTOList(List.of(model, model2)))
                .thenReturn(List.of(responseDTO, dto2));

        ResponseEntity<List<NotificationResponseDTO>> response =
                notificationController.getNotifications(10L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldPropagateNotificationNotFoundExceptionWhenNoNotifications() {
        when(notificationService.getNotifications(10L))
                .thenThrow(new NotificationNotFoundException(10L));

        assertThrows(NotificationNotFoundException.class,
                () -> notificationController.getNotifications(10L));
    }

    @Test
    void shouldPropagateExceptionForNonExistentUser() {
        when(notificationService.getNotifications(999L))
                .thenThrow(new NotificationNotFoundException(999L));

        NotificationNotFoundException ex = assertThrows(
                NotificationNotFoundException.class,
                () -> notificationController.getNotifications(999L)
        );

        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void shouldCallMapperAfterServiceOnGetNotifications() {
        when(notificationService.getNotifications(10L)).thenReturn(List.of(model));
        when(notificationMapper.toResponseDTOList(List.of(model))).thenReturn(List.of(responseDTO));

        notificationController.getNotifications(10L);

        verify(notificationService).getNotifications(10L);
        verify(notificationMapper).toResponseDTOList(List.of(model));
    }

    @Test
    void shouldReturn201WithCreatedNotification() {
        when(notificationMapper.toModel(requestDTO)).thenReturn(model);
        when(notificationService.createNotification(model)).thenReturn(model);
        when(notificationMapper.toResponseDTO(model)).thenReturn(responseDTO);

        ResponseEntity<NotificationResponseDTO> response =
                notificationController.createNotification(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Admin", response.getBody().getSender());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void shouldReturn201WithReadFalseOnCreation() {
        when(notificationMapper.toModel(requestDTO)).thenReturn(model);
        when(notificationService.createNotification(model)).thenReturn(model);
        when(notificationMapper.toResponseDTO(model)).thenReturn(responseDTO);

        ResponseEntity<NotificationResponseDTO> response =
                notificationController.createNotification(requestDTO);

        assertFalse(response.getBody().isRead());
    }

    @Test
    void shouldCallServiceOnceOnCreation() {
        when(notificationMapper.toModel(requestDTO)).thenReturn(model);
        when(notificationService.createNotification(model)).thenReturn(model);
        when(notificationMapper.toResponseDTO(model)).thenReturn(responseDTO);

        notificationController.createNotification(requestDTO);

        verify(notificationService, times(1)).createNotification(any());
    }

    @Test
    void shouldFollowCorrectCallOrderOnCreation() {
        when(notificationMapper.toModel(requestDTO)).thenReturn(model);
        when(notificationService.createNotification(model)).thenReturn(model);
        when(notificationMapper.toResponseDTO(model)).thenReturn(responseDTO);

        notificationController.createNotification(requestDTO);

        verify(notificationMapper).toModel(requestDTO);
        verify(notificationService).createNotification(model);
        verify(notificationMapper).toResponseDTO(model);
    }
}
