package com.escuela.techcup.core.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoalTest {

    @Test
    void testGetMinutePorDefectoEsCero() {
        Goal goal = new Goal();
        assertEquals(0, goal.getMinute());
    }

    @Test
    void testGetPlayerPorDefectoEsNulo() {
        Goal goal = new Goal();
        assertNull(goal.getPlayer());
    }

    @Test
    void testGoalNoEsNulo() {
        Goal goal = new Goal();
        assertNotNull(goal);
    }

    @Test
    void testImplementaMatchEvent() {
        Goal goal = new Goal();
        assertInstanceOf(MatchEvent.class, goal);
    }
}
