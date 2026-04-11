package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;
import com.escuela.techcup.persistence.mapper.tournament.SoccerFieldMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoccerFieldMapperTest {

    @Test
    void toEntity_mapsFieldsCorrectly() {
        SoccerField model = new SoccerField("sf-1", "Cancha Norte", "Bloque A");

        SoccerFieldEntity entity = SoccerFieldMapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("sf-1", entity.getId());
        assertEquals("Cancha Norte", entity.getName());
        assertEquals("Bloque A", entity.getLocation());
    }

    @Test
    void toModel_mapsFieldsCorrectly() {
        SoccerFieldEntity entity = new SoccerFieldEntity();
        entity.setId("sf-2");
        entity.setName("Cancha Sur");
        entity.setLocation("Bloque B");

        SoccerField model = SoccerFieldMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("sf-2", model.getId());
        assertEquals("Cancha Sur", model.getName());
        assertEquals("Bloque B", model.getLocation());
    }

    @Test
    void roundtrip_preservesData() {
        SoccerField original = new SoccerField("sf-3", "Cancha Central", "Piso 1");
        SoccerField result = SoccerFieldMapper.toModel(SoccerFieldMapper.toEntity(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getName(), result.getName());
        assertEquals(original.getLocation(), result.getLocation());
    }
}