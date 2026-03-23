package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testNumberOfValues() {
        assertEquals(4, Position.values().length);
    }

    @Test
    void testValuesExist() {
        assertNotNull(Position.valueOf("GOALKEEPER"));
        assertNotNull(Position.valueOf("DEFENDER"));
        assertNotNull(Position.valueOf("MIDFIELDER"));
        assertNotNull(Position.valueOf("FORWARD"));
    }

    @Test
    void testValueNames() {
        assertEquals("GOALKEEPER",   Position.GOALKEEPER.name());
        assertEquals("DEFENDER",   Position.DEFENDER.name());
        assertEquals("MIDFIELDER",   Position.MIDFIELDER.name());
        assertEquals("FORWARD", Position.FORWARD.name());
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Position.valueOf("INVALIDO"));
    }
}

