package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NotificationEntityTest {

    @Test
    void shouldCreateEntityWithBuilder() {
        NotificationEntity entity = NotificationEntity.builder()
                .id(1L)
                .userId(10L)
                .sender("Admin")
                .description("Test notification")
                .dateTime(LocalDateTime.of(2024, 4, 16, 10, 0))
                .read(false)
                .build();

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(10L, entity.getUserId());
        assertEquals("Admin", entity.getSender());
        assertFalse(entity.isRead());
    }

    @Test
    void shouldDefaultReadToFalse() {
        NotificationEntity entity = new NotificationEntity();
        entity.setRead(false);

        assertFalse(entity.isRead());
    }

    @Test
    void shouldCreateEntityWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.of(2024, 4, 16, 10, 0);
        NotificationEntity entity = new NotificationEntity(3L, 30L, "System", "Reminder", now, true);

        assertEquals(3L, entity.getId());
        assertEquals(30L, entity.getUserId());
        assertEquals("System", entity.getSender());
        assertTrue(entity.isRead());
    }

    @Test
    void shouldUpdateEntityFields() {
        NotificationEntity entity = NotificationEntity.builder()
                .id(1L)
                .read(false)
                .build();

        entity.setRead(true);
        entity.setDescription("Updated description");

        assertTrue(entity.isRead());
        assertEquals("Updated description", entity.getDescription());
    }

    @Test
    void shouldHandleNullFieldsGracefully() {
        NotificationEntity entity = NotificationEntity.builder().build();

        assertNull(entity.getId());
        assertNull(entity.getSender());
        assertNull(entity.getDescription());
        assertFalse(entity.isRead());
    }
}
