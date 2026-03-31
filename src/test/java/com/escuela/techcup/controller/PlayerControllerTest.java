package com.escuela.techcup.controller;

import com.escuela.techcup.controller.handler.GlobalExceptionHandler;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    private MockMvc mockMvc;

        @Mock
    private PlayerService playerService;

        @InjectMocks
        private PlayerController playerController;


    private Player playerMock;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(playerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        UserPlayer userPlayer = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2001, 4, 12), Gender.MALE, "pass");
        playerMock = new Player(userPlayer, Position.FORWARD, 9);
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

        private MockMultipartHttpServletRequestBuilder multipartWithPlayer(String path, String playerJson) {
                MockMultipartFile playerPart = new MockMultipartFile(
                        "player",
                        "",
                        MediaType.APPLICATION_JSON_VALUE,
                        playerJson.getBytes()
                );
                return multipart(path).file(playerPart);
        }


    @Test
        void testCreateSportsProfileStudent_returns201() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

                mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileStudent_returnsName() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void testCreateSportsProfileStudent_returnsMail() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(jsonPath("$.mail").value("pedro@test.com"));
    }

    @Test
    void testCreateSportsProfileStudent_returnsPosition() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(jsonPath("$.position").value("FORWARD"));
    }

    @Test
    void testCreateSportsProfileStudent_callsService() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")));

        verify(playerService, times(1)).createSportsProfileStudent(any(), any());
    }

    @Test
    void testCreateSportsProfileStudent_withoutName_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson(null, "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_invalidMail_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "no-es-mail", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_withoutSemester_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", null, 9, "FORWARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_zeroDorsal_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 0, "FORWARD")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_withoutPosition_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileStudent_serviceThrowsException_returns409() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any()))
                                .thenThrow(new TechcupException("Player already exists", HttpStatus.CONFLICT));

        mockMvc.perform(multipartWithPlayer("/api/players/students/sports-profile",
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD")))
                .andExpect(status().isConflict());
    }

    @Test
        void testCreateSportsProfileStudentWithPhoto_returns201() throws Exception {
        when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD").getBytes());

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/students/sports-profile")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileStudentWithPhoto_withoutPhoto_returns201() throws Exception {
                when(playerService.createSportsProfileStudent(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD").getBytes());

        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_PNG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/students/sports-profile")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileTeacher_returns201() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileTeacher_returnsName() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER")))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
    void testCreateSportsProfileTeacher_callsService() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER")));

        verify(playerService, times(1)).createSportsProfileTeacher(any(), any());
    }

    @Test
    void testCreateSportsProfileTeacher_withoutName_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson(null, "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacher_zeroDorsal_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 0, "GOALKEEPER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileTeacher_serviceThrowsException_returns409() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any()))
                                .thenThrow(new TechcupException("Player already exists", HttpStatus.CONFLICT));

        mockMvc.perform(multipartWithPlayer("/api/players/teachers/sports-profile",
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileFamiliar_returns201() throws Exception {
        when(playerService.createSportsProfileFamiliar(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/familiars/sports-profile",
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileFamiliar_callsService() throws Exception {
        when(playerService.createSportsProfileFamiliar(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/familiars/sports-profile",
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER")));

        verify(playerService, times(1)).createSportsProfileFamiliar(any(), any());
    }

    @Test
    void testCreateSportsProfileFamiliar_withoutPosition_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/familiars/sports-profile",
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileFamiliar_serviceThrowsException_returns409() throws Exception {
        when(playerService.createSportsProfileFamiliar(any(), any()))
                                .thenThrow(new TechcupException("Player already exists", HttpStatus.CONFLICT));

        mockMvc.perform(multipartWithPlayer("/api/players/familiars/sports-profile",
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateSportsProfileGraduate_returns201() throws Exception {
        when(playerService.createSportsProfileGraduate(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/graduates/sports-profile",
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateSportsProfileGraduate_callsService() throws Exception {
                when(playerService.createSportsProfileGraduate(any(), any())).thenReturn(playerMock);

        mockMvc.perform(multipartWithPlayer("/api/players/graduates/sports-profile",
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER")));

        verify(playerService, times(1)).createSportsProfileGraduate(any(), any());
    }

    @Test
    void testCreateSportsProfileGraduate_withoutPassword_returns400() throws Exception {
        mockMvc.perform(multipartWithPlayer("/api/players/graduates/sports-profile",
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", null, 11, "MIDFIELDER")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateSportsProfileGraduate_serviceThrowsException_returns409() throws Exception {
        when(playerService.createSportsProfileGraduate(any(), any()))
                                .thenThrow(new TechcupException("Player already exists", HttpStatus.CONFLICT));

        mockMvc.perform(multipartWithPlayer("/api/players/graduates/sports-profile",
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER")))
                .andExpect(status().isConflict());
    }

    @Test
        void testCreateSportsProfileStudentWithPhoto_corruptedImage_returns400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                studentPlayerDTOJson("Pedro", "pedro@test.com", "2001-04-12", "MALE", "pass", 5, 9, "FORWARD").getBytes());

        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/students/sports-profile")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
        void testCreateSportsProfileTeacherWithPhoto_returns201() throws Exception {
        when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/teachers/sports-profile")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileTeacherWithPhoto_withoutPhoto_returns201() throws Exception {
                when(playerService.createSportsProfileTeacher(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/teachers/sports-profile")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileTeacherWithPhoto_corruptedImage_returns400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Luis", "luis@test.com", "1985-03-10", "MALE", "pass", 5, "GOALKEEPER").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/teachers/sports-profile")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
        void testCreateSportsProfileFamiliarWithPhoto_returns201() throws Exception {
        when(playerService.createSportsProfileFamiliar(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/familiars/sports-profile")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileFamiliarWithPhoto_withoutPhoto_returns201() throws Exception {
                when(playerService.createSportsProfileFamiliar(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/familiars/sports-profile")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileFamiliarWithPhoto_corruptedImage_returns400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Maria", "maria@test.com", "1995-06-20", "FEMALE", "pass", 7, "DEFENDER").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/familiars/sports-profile")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
        void testCreateSportsProfileGraduateWithPhoto_returns201() throws Exception {
        when(playerService.createSportsProfileGraduate(any(), any())).thenReturn(playerMock);

        byte[] imagenBytes = getClass().getClassLoader().getResourceAsStream("test.jpg").readAllBytes();
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER").getBytes());
        MockMultipartFile photoPart = new MockMultipartFile(
                "profilePicture", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imagenBytes);

        mockMvc.perform(multipart("/api/players/graduates/sports-profile")
                .file(playerPart)
                .file(photoPart))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileGraduateWithPhoto_withoutPhoto_returns201() throws Exception {
                when(playerService.createSportsProfileGraduate(any(), any())).thenReturn(playerMock);

        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER").getBytes());
        MockMultipartFile fotoVacia = new MockMultipartFile(
                "profilePicture", "", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/players/graduates/sports-profile")
                .file(playerPart)
                .file(fotoVacia))
                .andExpect(status().isCreated());
    }

    @Test
        void testCreateSportsProfileGraduateWithPhoto_corruptedImage_returns400() throws Exception {
        MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                playerDTOJson("Ana", "ana@test.com", "1998-11-05", "FEMALE", "pass", 11, "MIDFIELDER").getBytes());
        MockMultipartFile fotoCorrupta = new MockMultipartFile(
                "profilePicture", "corrupta.jpg", MediaType.IMAGE_JPEG_VALUE,
                new byte[]{0x00, 0x01, 0x02, 0x03});

        mockMvc.perform(multipart("/api/players/graduates/sports-profile")
                .file(playerPart)
                .file(fotoCorrupta))
                .andExpect(status().isBadRequest());
    }

    @Test
        void testGetAllPlayers_returns200() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(playerMock));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk());
    }

    @Test
        void testGetAllPlayers_returnsList() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(playerMock));

        mockMvc.perform(get("/api/players"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
        void testGetAllPlayers_emptyList_returns200() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of());

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
        void testGetAllPlayers_callsService() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of());

        mockMvc.perform(get("/api/players"));

        verify(playerService, times(1)).getAllPlayers();
    }

    @Test
        void testGetPlayerByUserId_returns200() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"))
                .andExpect(status().isOk());
    }

    @Test
        void testGetPlayerByUserId_returnsName() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"))
                .andExpect(jsonPath("$.name").value("Pedro"));
    }

    @Test
        void testGetPlayerByUserId_notFound_returns404() throws Exception {
        when(playerService.getPlayerByUserId("u99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/players/u99"))
                .andExpect(status().isNotFound());
    }

    @Test
        void testGetPlayerByUserId_callsService() throws Exception {
        when(playerService.getPlayerByUserId("u1")).thenReturn(Optional.of(playerMock));

        mockMvc.perform(get("/api/players/u1"));

        verify(playerService, times(1)).getPlayerByUserId("u1");
    }
}

