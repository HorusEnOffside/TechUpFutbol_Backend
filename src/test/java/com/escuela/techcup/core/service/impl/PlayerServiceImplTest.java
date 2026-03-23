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

    @Mock
    private UserService userService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private StudentPlayerDTO validStudentDTO;
    private PlayerDTO         validPlayerDTO;
    private UserPlayer        stubUserPlayer;
    private BufferedImage     dummyImage;

    @BeforeEach
    void setUp() {
        stubUserPlayer = new Student(
            "user-001",
            "Test User",
            "test@escuela.edu",
            LocalDate.of(2000, 1, 1),
            Gender.MALE,
            5,
            "HashedPass1"
        );

        // DTO estudiante-jugador válido (password: ≥8 chars + mayúscula)
        validStudentDTO = new StudentPlayerDTO();
        validStudentDTO.setName("Juan Pérez");
        validStudentDTO.setMail("juan@escuela.edu");
        validStudentDTO.setDateOfBirth(LocalDate.of(2000, 6, 15));
        validStudentDTO.setGender(Gender.MALE);
        validStudentDTO.setPassword("Password1");
        validStudentDTO.setSemester(4);                   // semester: 1–10
        validStudentDTO.setDorsalNumber(10);              // dorsalNumber: > 0
        validStudentDTO.setPosition(Position.FORWARD);

        validPlayerDTO = new PlayerDTO(
            "Carlos Díaz",
            "carlos@escuela.edu",
            LocalDate.of(1985, 3, 20),
            Gender.MALE,
            "Password1",
            1,
            Position.GOALKEEPER
        );

        dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    /**
     *Pruebas perfil esstudiante
     */
    @Nested
    @DisplayName("createSportsProfileStudent")
    class CreateSportsProfileStudent {

        @Test
        @DisplayName("✅ Sin foto: retorna Player con posición, dorsal y estado DISPONIBLE")
        void shouldCreateStudentProfileWithoutPhoto() {
            // STUB: el servicio devuelve nuestro UserPlayer predefinido
            when(userService.createStudentUser(any(StudentUserDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO, null);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(Position.FORWARD);
            assertThat(result.getDorsalNumber()).isEqualTo(10);
            assertThat(result.getStatus()).isEqualTo(PlayerStatus.AVAILABLE);

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
                () -> playerService.createSportsProfileStudent(validStudentDTO, null));

            // MOCK: userService nunca fue invocado — la validación cortó el flujo
            verify(userService, never()).createStudentUser(any(StudentUserDTO.class));
        }

        @Test
        @DisplayName("❌ Dorsal negativo lanza ValidationException")
        void shouldThrowWhenDorsalIsNegative() {
            validStudentDTO.setDorsalNumber(-5);

            assertThrows(ValidationException.class,
                () -> playerService.createSportsProfileStudent(validStudentDTO, null));

            verify(userService, never()).createStudentUser(any(StudentUserDTO.class));
        }

        @Test
        @DisplayName("⚠️ Dorsal = 1 (límite mínimo válido) no lanza excepción")
        void shouldAcceptDorsalOne() {
            validStudentDTO.setDorsalNumber(1);
            when(userService.createStudentUser(any(StudentUserDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileStudent(validStudentDTO, null);

            assertThat(result.getDorsalNumber()).isEqualTo(1);
        }
    }

    /**
     * Pruebas perfil profesor
     */
    @Nested
    @DisplayName("createSportsProfileTeacher")
    class CreateSportsProfileTeacher {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createTeacherUser")
        void shouldCreateTeacherProfileWithoutPhoto() {
            when(userService.createTeacherUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileTeacher(validPlayerDTO, null);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(Position.GOALKEEPER);
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
                () -> playerService.createSportsProfileTeacher(validPlayerDTO, null));

            verify(userService, never()).createTeacherUser(any(PlayerDTO.class));
        }
    }

    /**
     * Pruebas perfil familiar
     */
    @Nested
    @DisplayName("createSportsProfileFamiliar")
    class CreateSportsProfileFamiliar {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createFamiliarUser")
        void shouldCreateFamiliarProfileWithoutPhoto() {
            when(userService.createFamiliarUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileFamiliar(validPlayerDTO, null);

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
                () -> playerService.createSportsProfileFamiliar(validPlayerDTO, null));

            verify(userService, never()).createFamiliarUser(any(PlayerDTO.class));
        }
    }

    /**
     * Pruenas para graduado
     */
    @Nested
    @DisplayName("createSportsProfileGraduate")
    class CreateSportsProfileGraduate {

        @Test
        @DisplayName("✅ Sin foto: retorna Player y delega a createGraduateUser")
        void shouldCreateGraduateProfileWithoutPhoto() {
            when(userService.createGraduateUser(any(PlayerDTO.class)))
                .thenReturn(stubUserPlayer);

            Player result = playerService.createSportsProfileGraduate(validPlayerDTO, null);

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
                () -> playerService.createSportsProfileGraduate(validPlayerDTO, null));

            verify(userService, never()).createGraduateUser(any(PlayerDTO.class));
        }
    }
}
