package com.escuela.techcup.controller;

import com.escuela.techcup.controller.dto.CreateSancionDTO;
import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.persistence.entity.tournament.SancionEntity;
import com.escuela.techcup.persistence.repository.tournament.SancionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SancionControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @Mock private SancionRepository sancionRepository;
    @InjectMocks private SancionController sancionController;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(sancionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    private CreateSancionDTO buildDTO(String tipo) {
        CreateSancionDTO dto = new CreateSancionDTO();
        dto.setTipo(tipo);
        dto.setEntidadId("uuid-entidad");
        dto.setEntidadNombre("Tigres FC");
        dto.setMotivo("Conducta inapropiada");
        dto.setFecha(LocalDate.of(2026, 5, 1));
        return dto;
    }

    // ── POST /api/sanciones ──────────────────────────────────────────────

    @Nested
    class CrearSancion {

        @Test
        void retorna201ConIdCuandoTipoEsEquipo() throws Exception {
            when(sancionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(buildDTO("equipo"))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        void retorna201ConIdCuandoTipoEsJugador() throws Exception {
            when(sancionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(buildDTO("jugador"))))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").isNotEmpty());
        }

        @Test
        void persisteTodosLosCampos() throws Exception {
            ArgumentCaptor<SancionEntity> captor = ArgumentCaptor.forClass(SancionEntity.class);
            when(sancionRepository.save(captor.capture())).thenAnswer(inv -> inv.getArgument(0));

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(buildDTO("equipo"))))
                    .andExpect(status().isCreated());

            SancionEntity saved = captor.getValue();
            assertThat(saved.getTipo()).isEqualTo("equipo");
            assertThat(saved.getEntidadId()).isEqualTo("uuid-entidad");
            assertThat(saved.getEntidadNombre()).isEqualTo("Tigres FC");
            assertThat(saved.getMotivo()).isEqualTo("Conducta inapropiada");
            assertThat(saved.getFecha()).isEqualTo(LocalDate.of(2026, 5, 1));
            assertThat(saved.getId()).isNotBlank();
        }

        @Test
        void retorna400CuandoTipoEsInvalido() throws Exception {
            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(buildDTO("arbitro"))))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void retorna400CuandoFaltaMotivo() throws Exception {
            CreateSancionDTO dto = buildDTO("equipo");
            dto.setMotivo("");

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void retorna400CuandoFaltaFecha() throws Exception {
            CreateSancionDTO dto = buildDTO("equipo");
            dto.setFecha(null);

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void retorna400CuandoFaltaEntidadId() throws Exception {
            CreateSancionDTO dto = buildDTO("equipo");
            dto.setEntidadId("");

            mockMvc.perform(post("/api/sanciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(dto)))
                    .andExpect(status().isBadRequest());
        }
    }
}
