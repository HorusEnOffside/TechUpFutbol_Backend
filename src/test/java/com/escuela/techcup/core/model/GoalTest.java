package com.escuela.techcup.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {

    @Test
    void testGetMinute() {
        Goal goal = new Goal("g1", 42, null, "desc");
        assertEquals(42, goal.getMinute());
    }

    @Test
    void testGetPlayerEsNuloSiNull() {
        Goal goal = new Goal("g1", 10, null, "desc");
        assertNull(goal.getPlayer());
    }

    @Test
    void testGoalNoEsNulo() {
        Goal goal = new Goal("g1", 1, null, "desc");
        assertNotNull(goal);
    }

    @Test
    void testImplementaMatchEvent() {
        Goal goal = new Goal("g1", 1, null, "desc");
        assertInstanceOf(MatchEvent.class, goal);
    }

    @Test
    void testGetDescription() {
        Goal goal = new Goal("g1", 1, null, "golazo");
        assertEquals("golazo", goal.getDescription());
    }

    @Test
    void testGetId() {
        Goal goal = new Goal("id123", 1, null, "desc");
        assertEquals("id123", goal.getId());
    }
}
