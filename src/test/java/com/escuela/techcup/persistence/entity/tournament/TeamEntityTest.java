package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

class TeamEntityTest {
    @Test
    void canSetAndGetFields() {
        TeamEntity entity = new TeamEntity();
        entity.setId("t1");
        entity.setName("Team");
        entity.setPlayers(List.of());
        assertEquals("t1", entity.getId());
        assertEquals("Team", entity.getName());
        assertNotNull(entity.getPlayers());
    }
}
