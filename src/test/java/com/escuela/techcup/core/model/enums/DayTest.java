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
        assertNotNull(Day.valueOf("LUNES"));
        assertNotNull(Day.valueOf("MARTES"));
        assertNotNull(Day.valueOf("MIERCOLES"));
        assertNotNull(Day.valueOf("JUEVES"));
        assertNotNull(Day.valueOf("VIERNES"));
        assertNotNull(Day.valueOf("SABADO"));
        assertNotNull(Day.valueOf("DOMINGO"));
    }

    @Test
    void testNombreDeValores() {
        assertEquals("LUNES",     Day.LUNES.name());
        assertEquals("MARTES",    Day.MARTES.name());
        assertEquals("MIERCOLES", Day.MIERCOLES.name());
        assertEquals("JUEVES",    Day.JUEVES.name());
        assertEquals("VIERNES",   Day.VIERNES.name());
        assertEquals("SABADO",    Day.SABADO.name());
        assertEquals("DOMINGO",   Day.DOMINGO.name());
    }

    @Test
    void testValueOfInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> Day.valueOf("INVALIDO"));
    }
}
