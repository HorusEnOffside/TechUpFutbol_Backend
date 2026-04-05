package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoalEntityTest {
    @Test
    void canSetAndGetFields() {
        GoalEntity entity = new GoalEntity();
        entity.setId("g1");
        entity.setMinute(10);
        entity.setDescription("desc");
        assertEquals("g1", entity.getId());
        assertEquals(10, entity.getMinute());
        assertEquals("desc", entity.getDescription());
    }
}
