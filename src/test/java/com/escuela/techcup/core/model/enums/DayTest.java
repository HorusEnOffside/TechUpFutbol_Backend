package com.escuela.techcup.core.model.enums;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DayTest {

    @Test
    void testCantidadDeValores() {
        assertEquals(7, Day.values().length);
    }

    @Test
    void testValoresExisten() {
        assertNotNull(Day.valueOf("MONDAY"));
        assertNotNull(Day.valueOf("TUESDAY"));
        assertNotNull(Day.valueOf("WEDNESDAY"));
        assertNotNull(Day.valueOf("THURSDAY"));
        assertNotNull(Day.valueOf("FRIDAY"));
        assertNotNull(Day.valueOf("SATURDAY"));
        assertNotNull(Day.valueOf("SUNDAY"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("MONDAY",    Day.MONDAY.name());
        assertEquals("TUESDAY",   Day.TUESDAY.name());
        assertEquals("WEDNESDAY", Day.WEDNESDAY.name());
        assertEquals("THURSDAY",  Day.THURSDAY.name());
        assertEquals("FRIDAY",    Day.FRIDAY.name());
        assertEquals("SATURDAY",  Day.SATURDAY.name());
        assertEquals("SUNDAY",    Day.SUNDAY.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Day.valueOf("INVALIDO"));
    }
}
