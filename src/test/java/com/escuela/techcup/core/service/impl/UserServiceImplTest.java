package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.ValidationException;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.Familiar;
import com.escuela.techcup.core.model.Graduate;
import com.escuela.techcup.core.model.Organizer;
import com.escuela.techcup.core.model.Referee;
import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.core.model.Teacher;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;

/**
 * Tests unitarios para UserServiceImpl.
 *
 *
 * Cobertura:
 *  ✅ Happy path  – cada tipo retorna la instancia correcta con ID y datos
 *  ❌ Error path  – validaciones lanzan ValidationException
 *  ⚠️ Edge case   – IDs únicos, semester en límites, BCrypt verificable
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    // ── DTOs base
    private UserDTO          validUserDTO;
    private StudentUserDTO   validStudentDTO;
    private UserPlayerDTO    validUserPlayerDTO;
    private BufferedImage    dummyImage;

    // Fecha de nacimiento en el pasado (requerido por DateUtil.isPast)
    private static final LocalDate PAST_DATE = LocalDate.of(1995, 5, 20);

    @BeforeEach
    void setUp() {
        validUserDTO = new UserDTO(
            "María López",
            "maria@escuela.edu",
            PAST_DATE,
            Gender.MUJER,
            "Password1"
        );

        validStudentDTO = new StudentUserDTO(
            "Pedro Gómez",
            "pedro@escuela.edu",
            PAST_DATE,
            Gender.MUJER,
            "Password1",
            4
        );

        validUserPlayerDTO = new UserPlayerDTO(
            "Ana Torres",
            "ana@escuela.edu",
            PAST_DATE,
            Gender.MUJER,
            "Password1"
        );

        dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Pruebas para administrador
     */
    @Nested
    @DisplayName("createAdminUser")
    class CreateAdminUser {

        @Test
        @DisplayName("✅ Retorna Administrator con ID no vacío y mail correcto")
        void shouldReturnAdministratorInstance() {
            User result = userService.createAdminUser(validUserDTO);

            assertThat(result).isInstanceOf(Administrator.class);
            assertThat(result.getId()).isNotBlank();
            assertThat(result.getMail()).isEqualTo("maria@escuela.edu");
            assertThat(result.getName()).isEqualTo("María López");
        }

        @Test
        @DisplayName("⚠️ La contraseña almacenada no está en claro (BCrypt)")
        void passwordShouldBeHashed() {
            User result = userService.createAdminUser(validUserDTO);

            // BCrypt produce hashes distintos: comparamos con matches(), no con equals()
            assertThat(result.getPassword())
                .isNotEqualTo("Password1")
                .startsWith("$2a$");   // prefijo BCrypt
        }

        @Test
        @DisplayName("⚠️ Dos usuarios creados con correos distintos tienen IDs únicos")
        void shouldGenerateUniqueIds() {
            User first  = userService.createAdminUser(validUserDTO);
            validUserDTO.setMail("maria2@escuela.edu");
            User second = userService.createAdminUser(validUserDTO);

            assertThat(first.getId()).isNotEqualTo(second.getId());
        }

        @Test
        @DisplayName("❌ No permite crear dos usuarios con el mismo correo")
        void shouldThrowWhenMailAlreadyExists() {
            userService.createAdminUser(validUserDTO);

            assertThrows(InvalidInputException.class,
                () -> userService.createOrganizerUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Mail sin '@' lanza ValidationException")
        void shouldThrowWhenMailHasNoAt() {
            validUserDTO.setMail("mariasinemail.edu");

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Mail sin '.' lanza ValidationException")
        void shouldThrowWhenMailHasNoDot() {
            validUserDTO.setMail("maria@sinpunto");

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Nombre vacío lanza ValidationException")
        void shouldThrowWhenNameIsBlank() {
            validUserDTO.setName("   ");

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Nombre nulo lanza ValidationException")
        void shouldThrowWhenNameIsNull() {
            validUserDTO.setName(null);

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Password < 8 caracteres lanza ValidationException")
        void shouldThrowWhenPasswordIsTooShort() {
            validUserDTO.setPassword("Pass1");

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }

        @Test
        @DisplayName("❌ Password sin mayúscula lanza ValidationException")
        void shouldThrowWhenPasswordHasNoUppercase() {
            validUserDTO.setPassword("password1");

            assertThrows(ValidationException.class,
                () -> userService.createAdminUser(validUserDTO));
        }
    }

    /**
     * Pruebas para Organizador
     */
    @Nested
    @DisplayName("createOrganizerUser")
    class CreateOrganizerUser {

        @Test
        @DisplayName("✅ Retorna Organizer con ID generado")
        void shouldReturnOrganizerInstance() {
            User result = userService.createOrganizerUser(validUserDTO);

            assertThat(result).isInstanceOf(Organizer.class);
            assertThat(result.getId()).isNotBlank();
        }
    }

    /**
     * Pruebas para arbitro
     */
    @Nested
    @DisplayName("createRefereeUser")
    class CreateRefereeUser {

        @Test
        @DisplayName("✅ Retorna Referee con nombre correcto")
        void shouldReturnRefereeInstance() {
            User result = userService.createRefereeUser(validUserDTO);

            assertThat(result).isInstanceOf(Referee.class);
            assertThat(result.getName()).isEqualTo(validUserDTO.getName());
        }
    }

    /**
     * Pruebas para estudiante
     */
    @Nested
    @DisplayName("createStudentUser")
    class CreateStudentUser {

        @Test
        @DisplayName("✅ Sin foto: retorna Student con semestre correcto")
        void shouldReturnStudentWithoutPhoto() {
            UserPlayer result = userService.createStudentUser(validStudentDTO);

            assertThat(result).isInstanceOf(Student.class);
            assertThat(((Student) result).getSemester()).isEqualTo(4);
            assertThat(result.getId()).isNotBlank();
        }

        @Test
        @DisplayName("✅ Con foto: retorna Student sin errores")
        void shouldReturnStudentWithPhoto() {
            UserPlayer result = userService.createStudentUser(validStudentDTO, dummyImage);

            assertThat(result).isInstanceOf(Student.class);
            assertThat(result.getId()).isNotBlank();
        }

        @Test
        @DisplayName("❌ Semester = 0 lanza ValidationException (requirePositive)")
        void shouldThrowWhenSemesterIsZero() {
            validStudentDTO.setSemester(0);

            assertThrows(ValidationException.class,
                () -> userService.createStudentUser(validStudentDTO));
        }

        @Test
        @DisplayName("❌ Semester negativo lanza ValidationException")
        void shouldThrowWhenSemesterIsNegative() {
            validStudentDTO.setSemester(-1);

            assertThrows(ValidationException.class,
                () -> userService.createStudentUser(validStudentDTO));
        }

        @Test
        @DisplayName("❌ Semester = 11 lanza ValidationException (> máximo 10)")
        void shouldThrowWhenSemesterExceedsMax() {
            validStudentDTO.setSemester(11);

            assertThrows(ValidationException.class,
                () -> userService.createStudentUser(validStudentDTO));
        }

        @Test
        @DisplayName("⚠️ Semester = 1 (límite inferior) no lanza excepción")
        void shouldAcceptSemesterOne() {
            validStudentDTO.setSemester(1);

            assertThat(userService.createStudentUser(validStudentDTO)).isNotNull();
        }

        @Test
        @DisplayName("⚠️ Semester = 10 (límite superior) no lanza excepción")
        void shouldAcceptSemesterTen() {
            validStudentDTO.setSemester(10);

            assertThat(userService.createStudentUser(validStudentDTO)).isNotNull();
        }
    }

    /**
     * Pruebas para profesor
     */
    @Nested
    @DisplayName("createTeacherUser")
    class CreateTeacherUser {

        @Test
        @DisplayName("✅ Sin foto: retorna Teacher con mail correcto")
        void shouldReturnTeacherWithoutPhoto() {
            UserPlayer result = userService.createTeacherUser(validUserPlayerDTO);

            assertThat(result).isInstanceOf(Teacher.class);
            assertThat(result.getMail()).isEqualTo("ana@escuela.edu");
        }

        @Test
        @DisplayName("✅ Con foto: retorna Teacher")
        void shouldReturnTeacherWithPhoto() {
            UserPlayer result = userService.createTeacherUser(validUserPlayerDTO, dummyImage);

            assertThat(result).isInstanceOf(Teacher.class);
        }

        @Test
        @DisplayName("❌ Mail inválido lanza ValidationException")
        void shouldThrowWhenMailIsInvalid() {
            validUserPlayerDTO.setMail("sinArroba.edu");

            assertThrows(ValidationException.class,
                () -> userService.createTeacherUser(validUserPlayerDTO));
        }
    }

    /**
     * Pruebas para familiar
     */
    @Nested
    @DisplayName("createFamiliarUser")
    class CreateFamiliarUser {

        @Test
        @DisplayName("✅ Sin foto: retorna Familiar con ID generado")
        void shouldReturnFamiliarWithoutPhoto() {
            UserPlayer result = userService.createFamiliarUser(validUserPlayerDTO);

            assertThat(result).isInstanceOf(Familiar.class);
            assertThat(result.getId()).isNotBlank();
        }

        @Test
        @DisplayName("✅ Con foto: retorna Familiar")
        void shouldReturnFamiliarWithPhoto() {
            UserPlayer result = userService.createFamiliarUser(validUserPlayerDTO, dummyImage);

            assertThat(result).isInstanceOf(Familiar.class);
        }

        @Test
        @DisplayName("❌ Password sin mayúscula lanza ValidationException")
        void shouldThrowWhenPasswordHasNoUppercase() {
            validUserPlayerDTO.setPassword("sinmayuscula1");

            assertThrows(ValidationException.class,
                () -> userService.createFamiliarUser(validUserPlayerDTO));
        }
    }

    /**
     * Pruebas para graduado
     */
    @Nested
    @DisplayName("createGraduateUser")
    class CreateGraduateUser {

        @Test
        @DisplayName("✅ Sin foto: retorna Graduate")
        void shouldReturnGraduateWithoutPhoto() {
            UserPlayer result = userService.createGraduateUser(validUserPlayerDTO);

            assertThat(result).isInstanceOf(Graduate.class);
        }

        @Test
        @DisplayName("✅ Con foto: retorna Graduate")
        void shouldReturnGraduateWithPhoto() {
            UserPlayer result = userService.createGraduateUser(validUserPlayerDTO, dummyImage);

            assertThat(result).isInstanceOf(Graduate.class);
        }

        @Test
        @DisplayName("❌ Nombre nulo lanza ValidationException")
        void shouldThrowWhenNameIsNull() {
            validUserPlayerDTO.setName(null);

            assertThrows(ValidationException.class,
                () -> userService.createGraduateUser(validUserPlayerDTO));
        }
    }
}
