package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SoccerFieldEntityTest {
    @Test
    void canSetAndGetFields() {
        SoccerFieldEntity entity = new SoccerFieldEntity();
        entity.setId("sf1");
        entity.setName("Field");
        entity.setLocation("Loc");
        assertEquals("sf1", entity.getId());
        assertEquals("Field", entity.getName());
        assertEquals("Loc", entity.getLocation());
    }
}
