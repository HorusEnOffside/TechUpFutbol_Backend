package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TournamentEntityTest {
    @Test
    void canSetAndGetFields() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId("t1");
        assertEquals("t1", entity.getId());
    }
}
