package com.escuela.techcup.controller;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.escuela.techcup.core.Handler.GlobalExceptionHandler;
import com.escuela.techcup.core.exception.InvalidImageException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Tests de integración de capa web para PlayerController.
 *
 * Herramientas:
 *  - @WebMvcTest     → levanta solo el contexto MVC (sin BD, sin servicios reales)
 *  - MockMvc         → simula peticiones HTTP sin levantar servidor
 *  - @MockBean       → reemplaza PlayerService en el contexto Spring (Mock)
 *  - @Import         → incluye GlobalExceptionHandler para probar los error paths HTTP
 *
 * Estrategia de dobles:
 *  - MOCK  → playerService: verificamos que se llamó (o no se llamó) según el caso.
 *  - STUB  → when(playerService.createXxx(...)).thenReturn(stubPlayer): devuelve
 *            un Player real para que PlayerMapper.toResponseDTO() funcione sin errores.
 *
 * Qué se verifica aquí (responsabilidad del controller):
 *  ✅ Happy path     → 201 CREATED + body con los campos esperados
 *  ❌ Error path     → 400 BAD REQUEST por @Valid (dorsal=0, campos vacíos)
 *  ❌ Error path     → 400 BAD REQUEST por InvalidImageException (imagen inválida)
 *  ⚠️ Edge case      → multipart con imagen válida delega al servicio correctamente
 *
 * Lo que NO se prueba aquí (ya cubierto en PlayerServiceImplTest):
 *  - Lógica de negocio interna del servicio
 *  - Reglas de validación de dominio (dorsal, semestre, etc.)
 */
@WebMvcTest(PlayerController.class)
@Import(GlobalExceptionHandler.class)
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // MOCK: nos importa QUÉ método del servicio se invoca y cuántas veces
    @MockBean
    private PlayerService playerService;

    private ObjectMapper objectMapper;
    private Player       stubPlayer;        // STUB: respuesta fija del servicio

    // ── JSON válidos para cada tipo ──────────────────────────────────────────

    // StudentPlayerDTO válido
    private static final String VALID_STUDENT_JSON = """
        {
            "name": "Juan Pérez",
            "mail": "juan@escuela.edu",
            "dateOfBirth": "2000-06-15",
            "gender": "MASCULINO",
            "password": "Password1",
            "semester": 4,
            "dorsalNumber": 10,
            "position": "DELANTERO"
        }
        """;

    // PlayerDTO válido (teacher / familiar / graduate comparten estructura)
    private static final String VALID_PLAYER_JSON = """
        {
            "name": "Carlos Díaz",
            "mail": "carlos@escuela.edu",
            "dateOfBirth": "1985-03-20",
            "gender": "MASCULINO",
            "password": "Password1",
            "dorsalNumber": 1,
            "position": "PORTERO"
        }
        """;

    // StudentPlayerDTO con dorsal inválido (0) → debe fallar @Valid
    private static final String INVALID_DORSAL_STUDENT_JSON = """
        {
            "name": "Juan Pérez",
            "mail": "juan@escuela.edu",
            "dateOfBirth": "2000-06-15",
            "gender": "MASCULINO",
            "password": "Password1",
            "semester": 4,
            "dorsalNumber": 0,
            "position": "DELANTERO"
        }
        """;

    // PlayerDTO con dorsal inválido
    private static final String INVALID_DORSAL_PLAYER_JSON = """
        {
            "name": "Carlos Díaz",
            "mail": "carlos@escuela.edu",
            "dateOfBirth": "1985-03-20",
            "gender": "MASCULINO",
            "password": "Password1",
            "dorsalNumber": 0,
            "position": "PORTERO"
        }
        """;

    // StudentPlayerDTO sin position (null) → @NotNull debe fallar
    private static final String MISSING_POSITION_JSON = """
        {
            "name": "Juan Pérez",
            "mail": "juan@escuela.edu",
            "dateOfBirth": "2000-06-15",
            "gender": "MASCULINO",
            "password": "Password1",
            "semester": 4,
            "dorsalNumber": 10
        }
        """;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // STUB: Player real con datos mínimos para que PlayerMapper.toResponseDTO() funcione
        UserPlayer stubUserPlayer = new Student(
            "user-001",
            "Juan Pérez",
            "juan@escuela.edu",
            LocalDate.of(2000, 6, 15),
            Gender.HOMBRE,
            4,
            "HashedPass1"
        );
        stubPlayer = new Player(stubUserPlayer, Position.DELANTERO, 10);
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/players/students/sports-profile
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("POST /students/sports-profile")
    class CreateStudentSportsProfile {

        @Test
        @DisplayName("✅ 201 CREATED con datos válidos")
        void shouldReturn201WhenValidStudent() throws Exception {
            // STUB: el servicio devuelve un Player con datos
            when(playerService.createSportsProfileStudent(any()))
                .thenReturn(stubPlayer);

            mockMvc.perform(post("/api/players/students/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_STUDENT_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("juan@escuela.edu"))
                .andExpect(jsonPath("$.dorsalNumber").value(10))
                .andExpect(jsonPath("$.position").value("DELANTERO"));

            // MOCK: el servicio fue invocado exactamente una vez
            verify(playerService).createSportsProfileStudent(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando dorsalNumber = 0 (@Valid)")
        void shouldReturn400WhenDorsalIsZero() throws Exception {
            mockMvc.perform(post("/api/players/students/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_DORSAL_STUDENT_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

            // MOCK: el servicio NUNCA fue llamado — @Valid cortó el flujo
            verify(playerService, never()).createSportsProfileStudent(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando position es null (@NotNull)")
        void shouldReturn400WhenPositionIsNull() throws Exception {
            mockMvc.perform(post("/api/players/students/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(MISSING_POSITION_JSON))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileStudent(any());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/players/students/sports-profile/with-photo
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("POST /students/sports-profile/with-photo")
    class CreateStudentSportsProfileWithPhoto {

        @Test
        @DisplayName("✅ 201 CREATED con imagen PNG válida")
        void shouldReturn201WithValidImage() throws Exception {
            when(playerService.createSportsProfileStudent(any(), any()))
                .thenReturn(stubPlayer);

            // Imagen PNG mínima válida (1x1 px) que ImageIO puede leer
            byte[] minimalPng = buildMinimalPng();

            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_STUDENT_JSON.getBytes()
            );
            MockMultipartFile imagePart = new MockMultipartFile(
                "profilePicture", "photo.png", "image/png", minimalPng
            );

            mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                    .file(playerPart)
                    .file(imagePart))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("juan@escuela.edu"));

            verify(playerService).createSportsProfileStudent(any(), any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando el archivo no es imagen válida")
        void shouldReturn400WhenImageIsInvalid() throws Exception {
            // Bytes que no son una imagen real → ImageIO.read() retorna null → InvalidImageException
            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_STUDENT_JSON.getBytes()
            );
            MockMultipartFile badImage = new MockMultipartFile(
                "profilePicture", "bad.png", "image/png", "not-an-image".getBytes()
            );

            mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                    .file(playerPart)
                    .file(badImage))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

            verify(playerService, never()).createSportsProfileStudent(any(), any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando la imagen está vacía")
        void shouldReturn400WhenImageIsEmpty() throws Exception {
            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_STUDENT_JSON.getBytes()
            );
            MockMultipartFile emptyImage = new MockMultipartFile(
                "profilePicture", "empty.png", "image/png", new byte[0]
            );

            mockMvc.perform(multipart("/api/players/students/sports-profile/with-photo")
                    .file(playerPart)
                    .file(emptyImage))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileStudent(any(), any());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/players/teachers/sports-profile
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("POST /teachers/sports-profile")
    class CreateTeacherSportsProfile {

        @Test
        @DisplayName("✅ 201 CREATED con datos válidos")
        void shouldReturn201WhenValidTeacher() throws Exception {
            Player teacherStub = buildStubPlayerWithMail("carlos@escuela.edu", Position.PORTERO, 1);
            when(playerService.createSportsProfileTeacher(any()))
                .thenReturn(teacherStub);

            mockMvc.perform(post("/api/players/teachers/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_PLAYER_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mail").value("carlos@escuela.edu"))
                .andExpect(jsonPath("$.dorsalNumber").value(1));

            verify(playerService).createSportsProfileTeacher(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando dorsalNumber = 0")
        void shouldReturn400WhenDorsalIsZero() throws Exception {
            mockMvc.perform(post("/api/players/teachers/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_DORSAL_PLAYER_JSON))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileTeacher(any());
        }

        @Test
        @DisplayName("✅ 201 CREATED con foto válida")
        void shouldReturn201WithPhoto() throws Exception {
            Player teacherStub = buildStubPlayerWithMail("carlos@escuela.edu", Position.PORTERO, 1);
            when(playerService.createSportsProfileTeacher(any(), any()))
                .thenReturn(teacherStub);

            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_PLAYER_JSON.getBytes()
            );
            MockMultipartFile imagePart = new MockMultipartFile(
                "profilePicture", "photo.png", "image/png", buildMinimalPng()
            );

            mockMvc.perform(multipart("/api/players/teachers/sports-profile/with-photo")
                    .file(playerPart)
                    .file(imagePart))
                .andExpect(status().isCreated());

            verify(playerService).createSportsProfileTeacher(any(), any());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/players/familiars/sports-profile
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("POST /familiars/sports-profile")
    class CreateFamiliarSportsProfile {

        @Test
        @DisplayName("✅ 201 CREATED con datos válidos")
        void shouldReturn201WhenValidFamiliar() throws Exception {
            Player familiarStub = buildStubPlayerWithMail("carlos@escuela.edu", Position.PORTERO, 1);
            when(playerService.createSportsProfileFamiliar(any()))
                .thenReturn(familiarStub);

            mockMvc.perform(post("/api/players/familiars/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_PLAYER_JSON))
                .andExpect(status().isCreated());

            verify(playerService).createSportsProfileFamiliar(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando dorsalNumber = 0")
        void shouldReturn400WhenDorsalIsZero() throws Exception {
            mockMvc.perform(post("/api/players/familiars/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_DORSAL_PLAYER_JSON))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileFamiliar(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST imagen inválida en with-photo")
        void shouldReturn400WhenImageInvalid() throws Exception {
            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_PLAYER_JSON.getBytes()
            );
            MockMultipartFile badImage = new MockMultipartFile(
                "profilePicture", "bad.png", "image/png", "not-an-image".getBytes()
            );

            mockMvc.perform(multipart("/api/players/familiars/sports-profile/with-photo")
                    .file(playerPart)
                    .file(badImage))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileFamiliar(any(), any());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // POST /api/players/graduates/sports-profile
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("POST /graduates/sports-profile")
    class CreateGraduateSportsProfile {

        @Test
        @DisplayName("✅ 201 CREATED con datos válidos")
        void shouldReturn201WhenValidGraduate() throws Exception {
            Player graduateStub = buildStubPlayerWithMail("carlos@escuela.edu", Position.PORTERO, 1);
            when(playerService.createSportsProfileGraduate(any()))
                .thenReturn(graduateStub);

            mockMvc.perform(post("/api/players/graduates/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_PLAYER_JSON))
                .andExpect(status().isCreated());

            verify(playerService).createSportsProfileGraduate(any());
        }

        @Test
        @DisplayName("❌ 400 BAD REQUEST cuando dorsalNumber = 0")
        void shouldReturn400WhenDorsalIsZero() throws Exception {
            mockMvc.perform(post("/api/players/graduates/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_DORSAL_PLAYER_JSON))
                .andExpect(status().isBadRequest());

            verify(playerService, never()).createSportsProfileGraduate(any());
        }

        @Test
        @DisplayName("✅ 201 CREATED con foto válida")
        void shouldReturn201WithPhoto() throws Exception {
            Player graduateStub = buildStubPlayerWithMail("carlos@escuela.edu", Position.PORTERO, 1);
            when(playerService.createSportsProfileGraduate(any(), any()))
                .thenReturn(graduateStub);

            MockMultipartFile playerPart = new MockMultipartFile(
                "player", "", MediaType.APPLICATION_JSON_VALUE,
                VALID_PLAYER_JSON.getBytes()
            );
            MockMultipartFile imagePart = new MockMultipartFile(
                "profilePicture", "photo.png", "image/png", buildMinimalPng()
            );

            mockMvc.perform(multipart("/api/players/graduates/sports-profile/with-photo")
                    .file(playerPart)
                    .file(imagePart))
                .andExpect(status().isCreated());

            verify(playerService).createSportsProfileGraduate(any(), any());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // GlobalExceptionHandler — comportamiento transversal
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("GlobalExceptionHandler — respuestas de error")
    class GlobalExceptionHandlerBehavior {

        @Test
        @DisplayName("⚠️ TechcupException se convierte en la respuesta HTTP con su status")
        void shouldReturnHttpStatusFromTechcupException() throws Exception {
            // El servicio lanza InvalidImageException (BAD_REQUEST) directamente
            when(playerService.createSportsProfileTeacher(any()))
                .thenThrow(new InvalidImageException("imagen inválida"));

            mockMvc.perform(post("/api/players/teachers/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(VALID_PLAYER_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("imagen inválida"))
                .andExpect(jsonPath("$.status").value(400));
        }

        @Test
        @DisplayName("⚠️ El body de error incluye campo 'timestamp'")
        void shouldIncludeTimestampInErrorBody() throws Exception {
            mockMvc.perform(post("/api/players/students/sports-profile")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(INVALID_DORSAL_STUDENT_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists());
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // Helpers
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Construye un Player stub con mail, posición y dorsal específicos.
     * Necesario para los tests de teacher/familiar/graduate donde el mail difiere.
     */
    private Player buildStubPlayerWithMail(String mail, Position position, int dorsal) {
        UserPlayer up = new Student(
            "user-002", "Carlos Díaz", mail,
            LocalDate.of(1985, 3, 20), Gender.HOMBRE, 1, "HashedPass1"
        );
        return new Player(up, position, dorsal);
    }

    /**
     * Genera un PNG de 1x1 pixel en bytes que ImageIO.read() puede leer
     * correctamente, simulando una imagen real en los tests multipart.
     *
     * Bytes del PNG mínimo estándar (header + IHDR + IDAT + IEND).
     */
    private byte[] buildMinimalPng() {
        return new byte[]{
            (byte)0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A, // PNG signature
            0x00, 0x00, 0x00, 0x0D, 0x49, 0x48, 0x44, 0x52,         // IHDR length + type
            0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,         // width=1, height=1
            0x08, 0x02, 0x00, 0x00, 0x00, (byte)0x90, 0x77, 0x53,   // bit depth, color type, CRC
            (byte)0xDE, 0x00, 0x00, 0x00, 0x0C, 0x49, 0x44, 0x41,   // IDAT length + type
            0x54, 0x08, (byte)0xD7, 0x63, (byte)0xF8, (byte)0xCF,   // IDAT data
            (byte)0xC0, 0x00, 0x00, 0x00, 0x02, 0x00, 0x01,         // IDAT data cont.
            (byte)0xE2, 0x21, (byte)0xBC, 0x33, 0x00, 0x00, 0x00,   // IDAT CRC
            0x00, 0x49, 0x45, 0x4E, 0x44, (byte)0xAE, 0x42,         // IEND
            0x60, (byte)0x82                                          // IEND CRC
        };
    }
}
