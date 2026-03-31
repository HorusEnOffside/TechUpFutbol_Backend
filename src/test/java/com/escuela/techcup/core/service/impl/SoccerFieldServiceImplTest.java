package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.core.service.SoccerFieldService;
import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;
import com.escuela.techcup.persistence.mapper.tournament.SoccerFieldMapper;
import com.escuela.techcup.persistence.repository.tournament.SoccerFieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SoccerFieldServiceImplTest {
    @Mock
    private SoccerFieldRepository soccerFieldRepository;

    @InjectMocks
    private SoccerFieldServiceImpl soccerFieldService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSoccerField_success() {
        String name = "Field A";
        String location = "Location A";
        when(soccerFieldRepository.existsByNameIgnoreCase(name)).thenReturn(false);
        when(soccerFieldRepository.save(any(SoccerFieldEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        SoccerField field = soccerFieldService.createSoccerField(name, location);
        assertNotNull(field.getId());
        assertEquals(name, field.getName());
        assertEquals(location, field.getLocation());
        verify(soccerFieldRepository).save(any(SoccerFieldEntity.class));
    }

    @Test
    void getSoccerFieldById_found() {
        String id = "id-1";
        SoccerFieldEntity entity = new SoccerFieldEntity();
        entity.setId(id);
        entity.setName("Field B");
        entity.setLocation("Location B");
        when(soccerFieldRepository.findById(id)).thenReturn(Optional.of(entity));

        SoccerField field = soccerFieldService.getSoccerFieldById(id);
        assertEquals(id, field.getId());
        assertEquals("Field B", field.getName());
        assertEquals("Location B", field.getLocation());
    }

    @Test
    void getSoccerFieldById_notFound() {
        String id = "not-exist";
        when(soccerFieldRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> soccerFieldService.getSoccerFieldById(id));
    }

    @Test
    void createSoccerField_duplicateName() {
        String name = "Field X";
        when(soccerFieldRepository.existsByNameIgnoreCase(name)).thenReturn(true);
        assertThrows(Exception.class, () -> soccerFieldService.createSoccerField(name, "Loc"));
    }
}
