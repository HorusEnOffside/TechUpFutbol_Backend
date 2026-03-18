package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.core.exception.ValidationException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.UserService;

/**
 * Tests unitarios para PlayerServiceImpl.
 * Cobertura:
 *  ✅ Happy path  – creación exitosa con y sin foto → Player con datos correctos
 *  ❌ Error path  – dorsal <= 0 lanza ValidationException antes de llamar userService
 *  ⚠️ Edge case   – foto nula redirige al método sin foto; dorsal = 1 (límite válido)
 */
@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    // ── MOCK: importa QUÉ método se llama y CON QUÉ parámetros ─────────────
    @Mock
    private UserService userService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    // ── Datos de prueba ──────────────────────────────────────────────────────
    private StudentPlayerDTO validStudentDTO;
    private PlayerDTO         validPlayerDTO;
    private UserPlayer        stubUserPlayer;   // STUB: respuesta fija de userService
    private BufferedImage     dummyImage;

    @BeforeEach
    void setUp() {
        // Stub mínimo de UserPlayer (Student es la subclase concreta más simple)
        stubUserPlayer = new Student(
            "user-001",
            "Test User",
            "test@escuela.edu",
            LocalDate.of(2000, 1, 1),
            Gender.HOMBRE,
            5,
            "HashedPass1"
        );

        // DTO estudiante-jugador válido (password: ≥8 chars + mayúscula)
        validStudentDTO = new StudentPlayerDTO(
            "Juan Pérez",
            "juan@escuela.edu",
            LocalDate.of(2000, 6, 15),
            Gender.HOMBRE,
            "Password1",
            4,                   // semester: 1–10
            10,                  // dorsalNumber: > 0
            Position.DELANTERO
        );

        // DTO genérico para teacher / familiar / graduate
        validPlayerDTO = new PlayerDTO(
            "Carlos Díaz",
            "carlos@escuela.edu",
            LocalDate.of(1985, 3, 20),
            Gender.HOMBRE,
            "Password1",
            1,
            Position.PORTERO
        );

        dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    // ════════════════════════════════════════════════════════════════════════
    // createSportsProfileStudent
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createSportsProfileStudent")
    class CreateSportsProfileStudent {

        @Test
        @DisplayName("✅ Sin foto: retorna Player con posición, dorsal y estado DISPONIBLE")
        void shouldCreateStudentProfileWithoutPhoto() {
            // STUB: el servicio devuelve nuestro UserPlayer predefinido
            when(userService.createStudentUser(any(StudentUserDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(Position.DELANTERO);
            assertThat(result.getDorsalNumber()).isEqualTo(10);
            assertThat(result.getStatus()).isEqualTo(PlayerStatus.DISPONIBLE);

            // MOCK: delegó exactamente 1 vez al método sin foto, nunca al de con foto
            verify(userService, times(1)).createStudentUser(any(StudentUserDTO.class));
            verify(userService, never())
                .createStudentUser(any(StudentUserDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("✅ Con foto válida: delega al método con BufferedImage")
        void shouldCreateStudentProfileWithPhoto() {
            when(userService.createStudentUser(any(StudentUserDTO.class), any(BufferedImage.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO, dummyImage);

            assertThat(result).isNotNull();
            // MOCK: se usó exclusivamente la versión CON foto
            verify(userService, times(1))
                .createStudentUser(any(StudentUserDTO.class), any(BufferedImage.class));
            verify(userService, never()).createStudentUser(any(StudentUserDTO.class));
        }

        @Test
        @DisplayName("⚠️ Foto nula: redirige al flujo sin foto (edge case)")
        void shouldFallbackToNoPhotoWhenPhotoIsNull() {
            when(userService.createStudentUser(any(StudentUserDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO, null);

            assertThat(result).isNotNull();
            // MOCK: el fallback invocó el método SIN foto
            verify(userService, times(1)).createStudentUser(any(StudentUserDTO.class));
            verify(userService, never())
                .createStudentUser(any(StudentUserDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("❌ Dorsal = 0 lanza ValidationException antes de llamar a userService")
        void shouldThrowWhenDorsalIsZero() {
            validStudentDTO.setDorsalNumber(0);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileStudent(validStudentDTO));

            // MOCK: userService nunca fue invocado — la validación cortó el flujo
            verify(userService, never()).createStudentUser(any(StudentUserDTO.class));
        }

        @Test
        @DisplayName("❌ Dorsal negativo lanza ValidationException")
        void shouldThrowWhenDorsalIsNegative() {
            validStudentDTO.setDorsalNumber(-5);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileStudent(validStudentDTO));

            verify(userService, never()).createStudentUser(any(StudentUserDTO.class));
        }

        @Test
        @DisplayName("⚠️ Dorsal = 1 (límite mínimo válido) no lanza excepción")
        void shouldAcceptDorsalOne() {
            validStudentDTO.setDorsalNumber(1);
            when(userService.createStudentUser(any(StudentUserDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO);

            assertThat(result.getDorsalNumber()).isEqualTo(1);
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // createSportsProfileTeacher
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createSportsProfileTeacher")
    class CreateSportsProfileTeacher {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createTeacherUser")
        void shouldCreateTeacherProfileWithoutPhoto() {
            when(userService.createTeacherUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileTeacher(validPlayerDTO);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(Position.PORTERO);
            verify(userService, times(1)).createTeacherUser(any(PlayerDTO.class));
        }

        @Test
        @DisplayName("✅ Con foto válida: delega al método con BufferedImage")
        void shouldCreateTeacherProfileWithPhoto() {
            when(userService.createTeacherUser(any(PlayerDTO.class), any(BufferedImage.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileTeacher(validPlayerDTO, dummyImage);

            assertThat(result).isNotNull();
            verify(userService, times(1))
                .createTeacherUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("⚠️ Foto nula: redirige al flujo sin foto")
        void shouldFallbackWhenPhotoIsNull() {
            when(userService.createTeacherUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            playerService.createSportsProfileTeacher(validPlayerDTO, null);

            verify(userService, times(1)).createTeacherUser(any(PlayerDTO.class));
            verify(userService, never())
                .createTeacherUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("❌ Dorsal = 0 lanza ValidationException")
        void shouldThrowWhenDorsalIsZero() {
            validPlayerDTO.setDorsalNumber(0);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileTeacher(validPlayerDTO));

            verify(userService, never()).createTeacherUser(any(PlayerDTO.class));
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // createSportsProfileFamiliar
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createSportsProfileFamiliar")
    class CreateSportsProfileFamiliar {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createFamiliarUser")
        void shouldCreateFamiliarProfileWithoutPhoto() {
            when(userService.createFamiliarUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileFamiliar(validPlayerDTO);

            assertThat(result).isNotNull();
            verify(userService, times(1)).createFamiliarUser(any(PlayerDTO.class));
        }

        @Test
        @DisplayName("✅ Con foto válida")
        void shouldCreateFamiliarProfileWithPhoto() {
            when(userService.createFamiliarUser(any(PlayerDTO.class), any(BufferedImage.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileFamiliar(validPlayerDTO, dummyImage);

            assertThat(result).isNotNull();
            verify(userService, times(1))
                .createFamiliarUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("⚠️ Foto nula: redirige al flujo sin foto")
        void shouldFallbackWhenPhotoIsNull() {
            when(userService.createFamiliarUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            playerService.createSportsProfileFamiliar(validPlayerDTO, null);

            verify(userService, times(1)).createFamiliarUser(any(PlayerDTO.class));
            verify(userService, never())
                .createFamiliarUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("❌ Dorsal = 0 lanza ValidationException")
        void shouldThrowWhenDorsalIsZero() {
            validPlayerDTO.setDorsalNumber(0);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileFamiliar(validPlayerDTO));

            verify(userService, never()).createFamiliarUser(any(PlayerDTO.class));
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // createSportsProfileGraduate
    // ════════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("createSportsProfileGraduate")
    class CreateSportsProfileGraduate {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createGraduateUser")
        void shouldCreateGraduateProfileWithoutPhoto() {
            when(userService.createGraduateUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileGraduate(validPlayerDTO);

            assertThat(result).isNotNull();
            verify(userService, times(1)).createGraduateUser(any(PlayerDTO.class));
        }

        @Test
        @DisplayName("✅ Con foto válida")
        void shouldCreateGraduateProfileWithPhoto() {
            when(userService.createGraduateUser(any(PlayerDTO.class), any(BufferedImage.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileGraduate(validPlayerDTO, dummyImage);

            assertThat(result).isNotNull();
            verify(userService, times(1))
                .createGraduateUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("⚠️ Foto nula: redirige al flujo sin foto")
        void shouldFallbackWhenPhotoIsNull() {
            when(userService.createGraduateUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            playerService.createSportsProfileGraduate(validPlayerDTO, null);

            verify(userService, times(1)).createGraduateUser(any(PlayerDTO.class));
            verify(userService, never())
                .createGraduateUser(any(PlayerDTO.class), any(BufferedImage.class));
        }

        @Test
        @DisplayName("❌ Dorsal = 0 lanza ValidationException")
        void shouldThrowWhenDorsalIsZero() {
            validPlayerDTO.setDorsalNumber(0);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileGraduate(validPlayerDTO));

            verify(userService, never()).createGraduateUser(any(PlayerDTO.class));
        }
    }
}
