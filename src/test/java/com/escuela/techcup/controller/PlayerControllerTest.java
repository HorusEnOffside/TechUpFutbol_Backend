package com.escuela.techcup.controller;

import com.escuela.techcup.config.SecurityConfig;
import com.escuela.techcup.controller.dto.PlayerResponseDTO;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
@Import(SecurityConfig.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;
    @MockBean
    private PasswordEncoder passwordEncoder;


    private ObjectMapper objectMapper;
    private Player playerMock;
    private PlayerResponseDTO playerResponseMock;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        UserPlayer userPlayer = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.HOMBRE, "pass");
        playerMock = new Player(userPlayer, Position.DELANTERO, 9);

        playerResponseMock = new PlayerResponseDTO(
                "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.HOMBRE,
                9, Position.DELANTERO,
                EnumSet.of(UserRole.BASEUSER));
    }

    private String studentPlayerDTOJson(String name, String mail, String dateOfBirth,
                                         String gender, String password,
                                         Integer semester, Integer dorsal, String position) {
        return String.format("""
                {
                    "name": %s,
                    "mail": %s,
                    "dateOfBirth": %s,
                    "gender": %s,
                    "password": %s,
                    "semester": %s,
                    "dorsalNumber": %s,
                    "position": %s
                }
                """,
                name == null ? "null" : "\"" + name + "\"",
                mail == null ? "null" : "\"" + mail + "\"",
                dateOfBirth == null ? "null" : "\"" + dateOfBirth + "\"",
                gender == null ? "null" : "\"" + gender + "\"",
                password == null ? "null" : "\"" + password + "\"",
                semester == null ? "null" : semester,
                dorsal == null ? "null" : dorsal,
                position == null ? "null" : "\"" + position + "\"");
    }

    private String playerDTOJson(String name, String mail, String dateOfBirth,
                                  String gender, String password,
                                  Integer dorsal, String position) {
        return String.format("""
                {
                    "name": %s,
                    "mail": %s,
                    "dateOfBirth": %s,
                    "gender": %s,
                    "password": %s,
                    "dorsalNumber": %s,
                    "position": %s
                }
                """,
                name == null ? "null" : "\"" + name + "\"",
                mail == null ? "null" : "\"" + mail + "\"",
                dateOfBirth == null ? "null" : "\"" + dateOfBirth + "\"",
                gender == null ? "null" : "\"" + gender + "\"",
                password == null ? "null" : "\"" + password + "\"",
                dorsal == null ? "null" : dorsal,
                position == null ? "null" : "\"" + position + "\"");
    }


    @Test
    void testCreateSportsProfileStudent_retorna201() throws Exception {
        when(playerService.createSportsProfileStudent(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileStudent_retornaNombre() throws Exception {
        when(playerService.createSportsProfileStudent(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void testCreateSportsProfileStudent_retornaMail() throws Exception {
        when(playerService.createSportsProfileStudent(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(jsonPath("$.mail").value("pedro@test.com"));
    }

    @Test
    void testCreateSportsProfileStudent_retornaPosition() throws Exception {
        when(playerService.createSportsProfileStudent(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(jsonPath("$.position").value("DELANTERO"));
    }

    @Test
    void testCreateSportsProfileStudent_llamaAlServicio() throws Exception {
        when(playerService.createSportsProfileStudent(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")));

        verify(playerService, times(1)).createSportsProfileStudent(any());
    }

    @Test
    void testCreateSportsProfileStudent_sinName_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson(null, "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_mailInvalido_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "no-es-mail", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_sinSemester_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", null, 9, "DELANTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_dorsalCero_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 0, "DELANTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_sinPosition_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_servicioLanzaExcepcion_retorna409() throws Exception {
        when(playerService.createSportsProfileStudent(any()))
                .thenThrow(new TechcupException("Jugador ya existe", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/players/students/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileStudentWithPhoto_retorna201() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO").getBytes());

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileStudentWithPhoto_sinFoto_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO").getBytes());

        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_PNG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacher_retorna201() throws Exception {
        when(playerService.createSportsProfileTeacher(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileTeacher_retornaNombre() throws Exception {
        when(playerService.createSportsProfileTeacher(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO")))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void testCreateSportsProfileTeacher_llamaAlServicio() throws Exception {
        when(playerService.createSportsProfileTeacher(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO")));

        verify(playerService, times(1)).createSportsProfileTeacher(any());
    }

    @Test
    void testCreateSportsProfileTeacher_sinName_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson(null, "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacher_dorsalCero_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 0, "PORTERO")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacher_servicioLanzaExcepcion_retorna409() throws Exception {
        when(playerService.createSportsProfileTeacher(any()))
                .thenThrow(new TechcupException("Jugador ya existe", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/players/teachers/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileFamiliar_retorna201() throws Exception {
        when(playerService.createSportsProfileFamiliar(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/familiars/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileFamiliar_llamaAlServicio() throws Exception {
        when(playerService.createSportsProfileFamiliar(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/familiars/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA")));

        verify(playerService, times(1)).createSportsProfileFamiliar(any());
    }

    @Test
    void testCreateSportsProfileFamiliar_sinPosition_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/familiars/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileFamiliar_servicioLanzaExcepcion_retorna409() throws Exception {
        when(playerService.createSportsProfileFamiliar(any()))
                .thenThrow(new TechcupException("Jugador ya existe", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/players/familiars/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileGraduate_retorna201() throws Exception {
        when(playerService.createSportsProfileGraduate(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/graduates/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileGraduate_llamaAlServicio() throws Exception {
        when(playerService.createSportsProfileGraduate(any())).thenReturn(playerMock);

        mockMvc.perform(post("/api/players/graduates/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE")));

        verify(playerService, times(1)).createSportsProfileGraduate(any());
    }

    @Test
    void testCreateSportsProfileGraduate_sinPassword_retorna400() throws Exception {
        mockMvc.perform(post("/api/players/graduates/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", null, 11, "VOLANTE")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileGraduate_servicioLanzaExcepcion_retorna409() throws Exception {
        when(playerService.createSportsProfileGraduate(any()))
                .thenThrow(new TechcupException("Jugador ya existe", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/players/graduates/sports-profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileStudentWithPhoto_imagenCorrupta_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "HOMBRE", "pass", 5, 9, "DELANTERO").getBytes());

        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacherWithPhoto_retorna201() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/teachers/sports-profile/with-photo")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileTeacherWithPhoto_sinFoto_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/teachers/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacherWithPhoto_imagenCorrupta_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "HOMBRE", "pass", 5, "PORTERO").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/teachers/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileFamiliarWithPhoto_retorna201() throws Exception {
        when(playerService.createSportsProfileFamiliar(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/familiars/sports-profile/with-photo")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileFamiliarWithPhoto_sinFoto_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/familiars/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileFamiliarWithPhoto_imagenCorrupta_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "MUJER", "pass", 7, "DEFENSA").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/familiars/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileGraduateWithPhoto_retorna201() throws Exception {
        when(playerService.createSportsProfileGraduate(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/graduates/sports-profile/with-photo")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileGraduateWithPhoto_sinFoto_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/graduates/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileGraduateWithPhoto_imagenCorrupta_retorna400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "MUJER", "pass", 11, "VOLANTE").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/graduates/sports-profile/with-photo")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllPlayers_retorna200() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(playerMock));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllPlayers_retornaLista() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(playerMock));

        mockMvc.perform(get("/api/players"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetAllPlayers_listaVacia_retorna200() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of());

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllPlayers_llamaAlServicio() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of());

        mockMvc.perform(get("/api/players"));

        verify(playerService, times(1)).getAllPlayers();
    }

    @Test
    void testGetPlayerByUserId_retorna200() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPlayerByUserId_retornaNombre() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void testGetPlayerByUserId_noExiste_retorna404() throws Exception {
        when(playerService.getPlayerByUserId("u99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/players/u99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPlayerByUserId_llamaAlServicio() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"));

        verify(playerService, times(1)).getPlayerByUserId("u1");
    }
}
