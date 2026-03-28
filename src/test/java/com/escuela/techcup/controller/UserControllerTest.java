package com.escuela.techcup.controller;

import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.Organizer;
import com.escuela.techcup.core.model.Referee;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.mock.web.MockMultipartFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private Administrator adminMock;
    private Organizer organizerMock;
    private Referee refereeMock;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setValidator(validator)
            .build();

        adminMock = new Administrator("u-admin", "Admin", "admin@test.com", LocalDate.of(1990, 1, 10), Gender.MALE, "encoded");
        adminMock.setPrimaryRole(UserRole.ADMIN);

        organizerMock = new Organizer("u-org", "Organizer", "org@test.com", LocalDate.of(1992, 2, 20), Gender.FEMALE, "encoded");
        organizerMock.setPrimaryRole(UserRole.ORGANIZER);

        refereeMock = new Referee("u-ref", "Referee", "ref@test.com", LocalDate.of(1991, 3, 15), Gender.MALE, "encoded");
        refereeMock.setPrimaryRole(UserRole.REFEREE);
    }

    private String userJson(String name, String mail, String dateOfBirth, String gender, String password) {
        return String.format("""
                {
                    "name": %s,
                    "mail": %s,
                    "dateOfBirth": %s,
                    "gender": %s,
                    "password": %s
                }
                """,
            name == null ? "null" : "\"" + name + "\"",
            mail == null ? "null" : "\"" + mail + "\"",
            dateOfBirth == null ? "null" : "\"" + dateOfBirth + "\"",
            gender == null ? "null" : "\"" + gender + "\"",
            password == null ? "null" : "\"" + password + "\"");
    }

    @Test
    void testCreateAdminUser_returns201() throws Exception {
        when(userService.createAdminUser(any(), eq(null))).thenReturn(adminMock);

        mockMvc.perform(multipart("/api/users/admin")
                        .file(userPart("Admin", "admin@test.com", "1990-01-10", "MALE", "Password1")))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateAdminUser_returnsResponseBody() throws Exception {
        when(userService.createAdminUser(any(), eq(null))).thenReturn(adminMock);

        mockMvc.perform(multipart("/api/users/admin")
                        .file(userPart("Admin", "admin@test.com", "1990-01-10", "MALE", "Password1")))
                .andExpect(jsonPath("$.name").value("Admin"))
                .andExpect(jsonPath("$.mail").value("admin@test.com"));
    }

    @Test
    void testCreateAdminUser_callsService() throws Exception {
        when(userService.createAdminUser(any(), eq(null))).thenReturn(adminMock);

        mockMvc.perform(multipart("/api/users/admin")
                .file(userPart("Admin", "admin@test.com", "1990-01-10", "MALE", "Password1")));

        verify(userService, times(1)).createAdminUser(any(), eq(null));
    }

    @Test
    void testCreateAdminUser_invalidBody_returns400() throws Exception {
        mockMvc.perform(multipart("/api/users/admin")
                        .file(userPart(null, "admin@test.com", "1990-01-10", "MALE", "Password1")))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createAdminUser(any(), eq(null));
    }

    @Test
    void testCreateAdminUser_serviceThrows_returns409() throws Exception {
        when(userService.createAdminUser(any(), eq(null)))
                .thenThrow(new TechcupException("duplicate user", HttpStatus.CONFLICT));

        mockMvc.perform(multipart("/api/users/admin")
                        .file(userPart("Admin", "admin@test.com", "1990-01-10", "MALE", "Password1")))
                .andExpect(status().isConflict());
    }

    @Test
    void testCreateOrganizerUser_returns201() throws Exception {
        when(userService.createOrganizerUser(any(), eq(null))).thenReturn(organizerMock);

        mockMvc.perform(multipart("/api/users/organizer")
                        .file(userPart("Organizer", "org@test.com", "1992-02-20", "FEMALE", "Password1")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("org@test.com"));
    }

    @Test
    void testCreateOrganizerUser_callsService() throws Exception {
        when(userService.createOrganizerUser(any(), eq(null))).thenReturn(organizerMock);

        mockMvc.perform(multipart("/api/users/organizer")
                .file(userPart("Organizer", "org@test.com", "1992-02-20", "FEMALE", "Password1")));

        verify(userService, times(1)).createOrganizerUser(any(), eq(null));
    }

    @Test
    void testCreateOrganizerUser_invalidMail_returns400() throws Exception {
        mockMvc.perform(multipart("/api/users/organizer")
                        .file(userPart("Organizer", "invalid-mail", "1992-02-20", "FEMALE", "Password1")))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createOrganizerUser(any(), eq(null));
    }

    @Test
    void testCreateRefereeUser_returns201() throws Exception {
        when(userService.createRefereeUser(any(), eq(null))).thenReturn(refereeMock);

        mockMvc.perform(multipart("/api/users/referee")
                        .file(userPart("Referee", "ref@test.com", "1991-03-15", "MALE", "Password1")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("ref@test.com"));
    }

    @Test
    void testCreateRefereeUser_callsService() throws Exception {
        when(userService.createRefereeUser(any(), eq(null))).thenReturn(refereeMock);

        mockMvc.perform(multipart("/api/users/referee")
                .file(userPart("Referee", "ref@test.com", "1991-03-15", "MALE", "Password1")));

        verify(userService, times(1)).createRefereeUser(any(), eq(null));
    }

    @Test
    void testCreateRefereeUser_missingPassword_returns400() throws Exception {
        mockMvc.perform(multipart("/api/users/referee")
                        .file(userPart("Referee", "ref@test.com", "1991-03-15", "MALE", null)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).createRefereeUser(any(), eq(null));
    }

    @Test
    void testGetAllUsers_returns200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(adminMock, organizerMock));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers_returnsList() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(adminMock, organizerMock));

        mockMvc.perform(get("/api/users"))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].mail").value("admin@test.com"))
            .andExpect(jsonPath("$[1].mail").value("org@test.com"));
    }

    @Test
    void testGetAllUsers_emptyList_returns200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllUsers_callsService() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById_returns200() throws Exception {
        when(userService.getUserById("u-admin")).thenReturn(Optional.of(adminMock));

        mockMvc.perform(get("/api/users/u-admin"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetUserById_returnsBody() throws Exception {
        when(userService.getUserById("u-admin")).thenReturn(Optional.of(adminMock));

        mockMvc.perform(get("/api/users/u-admin"))
            .andExpect(jsonPath("$.name").value("Admin"))
            .andExpect(jsonPath("$.mail").value("admin@test.com"));
    }

    @Test
    void testGetUserById_notFound_returns404() throws Exception {
        when(userService.getUserById("u-missing")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/u-missing"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById_callsService() throws Exception {
        when(userService.getUserById("u-admin")).thenReturn(Optional.of(adminMock));

        mockMvc.perform(get("/api/users/u-admin"));

        verify(userService, times(1)).getUserById("u-admin");
    }

    private MockMultipartFile userPart(String name, String mail, String dateOfBirth, String gender, String password) throws Exception {
        String json = String.format("""
        {
            "name": %s,
            "mail": %s,
            "dateOfBirth": %s,
            "gender": %s,
            "password": %s
        }
        """,
                name == null ? "null" : "\"" + name + "\"",
                mail == null ? "null" : "\"" + mail + "\"",
                dateOfBirth == null ? "null" : "\"" + dateOfBirth + "\"",
                gender == null ? "null" : "\"" + gender + "\"",
                password == null ? "null" : "\"" + password + "\""
        );
        return new MockMultipartFile("user", "", MediaType.APPLICATION_JSON_VALUE, json.getBytes());
    }
}
