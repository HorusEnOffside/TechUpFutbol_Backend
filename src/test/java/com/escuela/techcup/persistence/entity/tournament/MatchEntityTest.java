package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class MatchEntityTest {
    @Test
    void canSetAndGetFields() {
        MatchEntity entity = new MatchEntity();
        entity.setId("m1");
        entity.setDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertEquals("m1", entity.getId());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), entity.getDateTime());
    }
}
