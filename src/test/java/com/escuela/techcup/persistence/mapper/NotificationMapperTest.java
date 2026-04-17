package com.escuela.techcup.persistence.mapper;


import com.escuela.techcup.controller.dto.NotificationDTO;
import com.escuela.techcup.controller.dto.NotificationResponseDTO;
import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import com.escuela.techcup.persistence.mapper.tournament.NotificationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperTest {

    private NotificationMapper mapper;
    private LocalDateTime fixedDateTime;

    @BeforeEach
    void setUp() {
        mapper = new NotificationMapper();
        fixedDateTime = LocalDateTime.of(2024, 4, 16, 10, 0);
    }

    @Test
    void shouldMapEntityToModel() {
        NotificationEntity entity = NotificationEntity.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test").dateTime(fixedDateTime).read(false)
                .build();

        Notification model = mapper.toModel(entity);

        assertEquals(entity.getId(), model.getId());
        assertEquals(entity.getUserId(), model.getUserId());
        assertEquals(entity.getSender(), model.getSender());
        assertEquals(entity.getDescription(), model.getDescription());
        assertEquals(entity.getDateTime(), model.getDateTime());
        assertEquals(entity.isRead(), model.isRead());
    }

    @Test
    void shouldMapEntityToModelPreservingReadTrue() {
        NotificationEntity entity = NotificationEntity.builder()
                .id(2L).read(true).build();

        Notification model = mapper.toModel(entity);

        assertTrue(model.isRead());
    }

    @Test
    void shouldMapModelToEntity() {
        Notification model = Notification.builder()
                .id(1L).userId(10L).sender("Admin")
                .description("Test").dateTime(fixedDateTime).read(false)
                .build();

        NotificationEntity entity = mapper.toEntity(model);

        assertEquals(model.getId(), entity.getId());
        assertEquals(model.getUserId(), entity.getUserId());
        assertEquals(model.getSender(), entity.getSender());
        assertEquals(model.getDescription(), entity.getDescription());
        assertEquals(model.getDateTime(), entity.getDateTime());
    }


    @Test
    void shouldMapRequestDTOToModelWithReadFalse() {
        NotificationDTO dto = NotificationDTO.builder()
                .userId(10L).sender("Admin")
                .description("Test").dateTime(fixedDateTime)
                .build();

        Notification model = mapper.toModel(dto);

        assertEquals(dto.getUserId(), model.getUserId());
        assertEquals(dto.getSender(), model.getSender());
        assertEquals(dto.getDescription(), model.getDescription());
        assertEquals(dto.getDateTime(), model.getDateTime());
        assertFalse(model.isRead());
    }

    @Test
    void shouldMapRequestDTOWithNullDateTimeToModel() {
        NotificationDTO dto = NotificationDTO.builder()
                .userId(5L).sender("Bot").description("Hello").dateTime(null)
                .build();

        Notification model = mapper.toModel(dto);

        assertNull(model.getDateTime());
        assertFalse(model.isRead());
    }

    @Test
    void shouldMapModelToResponseDTO() {
        Notification model = Notification.builder()
                .id(1L).sender("Admin").description("Test")
                .dateTime(fixedDateTime).read(false)
                .build();

        NotificationResponseDTO dto = mapper.toResponseDTO(model);

        assertEquals(model.getId(), dto.getId());
        assertEquals(model.getSender(), dto.getSender());
        assertEquals(model.getDescription(), dto.getDescription());
        assertEquals(model.getDateTime(), dto.getDateTime());
        assertFalse(dto.isRead());
    }

    @Test
    void shouldMapModelToResponseDTOWithReadTrue() {
        Notification model = Notification.builder()
                .id(3L).sender("System").read(true).build();

        NotificationResponseDTO dto = mapper.toResponseDTO(model);

        assertTrue(dto.isRead());
    }

    @Test
    void shouldMapEntityListToModelList() {
        List<NotificationEntity> entities = List.of(
                NotificationEntity.builder().id(1L).sender("Admin").build(),
                NotificationEntity.builder().id(2L).sender("System").build()
        );

        List<Notification> models = mapper.toModelList(entities);

        assertEquals(2, models.size());
        assertEquals("Admin", models.get(0).getSender());
        assertEquals("System", models.get(1).getSender());
    }

    @Test
    void shouldReturnEmptyListWhenEntityListIsEmpty() {
        List<Notification> models = mapper.toModelList(Collections.emptyList());

        assertNotNull(models);
        assertTrue(models.isEmpty());
    }

    @Test
    void shouldMapModelListToResponseDTOList() {
        List<Notification> models = List.of(
                Notification.builder().id(1L).sender("Admin").build(),
                Notification.builder().id(2L).sender("System").build()
        );

        List<NotificationResponseDTO> dtos = mapper.toResponseDTOList(models);

        assertEquals(2, dtos.size());
        assertEquals("Admin", dtos.get(0).getSender());
        assertEquals("System", dtos.get(1).getSender());
    }

    @Test
    void shouldReturnEmptyListWhenModelListIsEmpty() {
        List<NotificationResponseDTO> dtos = mapper.toResponseDTOList(Collections.emptyList());

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }
}
