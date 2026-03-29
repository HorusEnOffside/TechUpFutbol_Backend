package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

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
import com.escuela.techcup.persistence.repository.users.*;
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


    @Mock
    private UserRepository userRepository;
    @Mock
    private AdministratorRepository administratorRepository;
    @Mock
    private OrganizerRepository organizerRepository;
    @Mock
    private RefereeRepository refereeRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private FamiliarRepository familiarRepository;
    @Mock
    private GraduateRepository graduateRepository;

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
            Gender.FEMALE,
            "Password1"
        );

        validStudentDTO = new StudentUserDTO(
            "Pedro Gómez",
            "pedro@escuela.edu",
            PAST_DATE,
            Gender.FEMALE,
            "Password1",
            4
        );

        validUserPlayerDTO = new UserPlayerDTO(
            "Ana Torres",
            "ana@escuela.edu",
            PAST_DATE,
            Gender.FEMALE,
            "Password1"
        );

        dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }


    // --- ADMIN ---
    @Test
    void shouldReturnAdministratorInstance() {
        when(administratorRepository.save(org.mockito.ArgumentMatchers.any())).thenAnswer(inv -> inv.getArgument(0));
        User result = userService.createAdminUser(validUserDTO, null);
        assertThat(result).isInstanceOf(Administrator.class);
        assertThat(result.getId()).isNotBlank();
        assertThat(result.getMail()).isEqualTo("maria@escuela.edu");
        assertThat(result.getName()).isEqualTo("María López");
    }

    @Test
    void passwordShouldBeHashed() {
        User result = userService.createAdminUser(validUserDTO, null);
        assertThat(result.getPassword())
            .isNotEqualTo("Password1")
            .startsWith("$2a$");
    }

    @Test
    void shouldGenerateUniqueIds() {
        User first  = userService.createAdminUser(validUserDTO, null);
        validUserDTO.setMail("maria2@escuela.edu");
        User second = userService.createAdminUser(validUserDTO, null);
        assertThat(first.getId()).isNotEqualTo(second.getId());
    }

    @Test
    void shouldThrowWhenMailAlreadyExists() {
        userService.createAdminUser(validUserDTO, null);
        when(userRepository.existsByMailIgnoreCase("maria@escuela.edu")).thenReturn(true);
        assertThrows(InvalidInputException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }

    @Test
    void shouldThrowWhenMailHasNoAt() {
        validUserDTO.setMail("mariasinemail.edu");
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }

    @Test
    void shouldThrowWhenMailHasNoDot() {
        validUserDTO.setMail("maria@sinpunto");
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        validUserDTO.setName("   ");
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        validUserDTO.setName(null);
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }

    @Test
    void shouldThrowWhenPasswordIsTooShort() {
        validUserDTO.setPassword("Pass1");
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }


    @Test
    void shouldThrowWhenPasswordHasNoUppercasePlayerDTO() {
        validUserPlayerDTO.setPassword("sinmayuscula1");
        assertThrows(ValidationException.class,
            () -> userService.createFamiliarUser(validUserPlayerDTO, null));
    }

    // --- ORGANIZER ---
    @Test
    void shouldReturnOrganizerInstance() {
        User result = userService.createOrganizerUser(validUserDTO, null);
        assertThat(result).isInstanceOf(Organizer.class);
        assertThat(result.getId()).isNotBlank();
    }

    // --- REFEREE ---
    @Test
    void shouldReturnRefereeInstance() {
        User result = userService.createRefereeUser(validUserDTO, null);
        assertThat(result).isInstanceOf(Referee.class);
        assertThat(result.getName()).isEqualTo(validUserDTO.getName());
    }

    // --- STUDENT ---
    @Test
    void shouldReturnStudentWithoutPhoto() {
        UserPlayer result = userService.createStudentUser(validStudentDTO, null);
        assertThat(result).isInstanceOf(Student.class);
        assertThat(((Student) result).getSemester()).isEqualTo(4);
        assertThat(result.getId()).isNotBlank();
    }

    @Test
    void shouldReturnStudentWithPhoto() {
        UserPlayer result = userService.createStudentUser(validStudentDTO, dummyImage);
        assertThat(result).isInstanceOf(Student.class);
        assertThat(result.getId()).isNotBlank();
    }

    @Test
    void shouldThrowWhenSemesterIsZero() {
        validStudentDTO.setSemester(0);
        assertThrows(ValidationException.class,
            () -> userService.createStudentUser(validStudentDTO, null));
    }

    @Test
    void shouldThrowWhenSemesterIsNegative() {
        validStudentDTO.setSemester(-1);
        assertThrows(ValidationException.class,
            () -> userService.createStudentUser(validStudentDTO, null));
    }

    @Test
    void shouldThrowWhenSemesterExceedsMax() {
        validStudentDTO.setSemester(11);
        assertThrows(ValidationException.class,
            () -> userService.createStudentUser(validStudentDTO, null));
    }

    @Test
    void shouldAcceptSemesterOne() {
        validStudentDTO.setSemester(1);
        assertThat(userService.createStudentUser(validStudentDTO, null)).isNotNull();
    }

    @Test
    void shouldAcceptSemesterTen() {
        validStudentDTO.setSemester(10);
        assertThat(userService.createStudentUser(validStudentDTO, null)).isNotNull();
    }

    @Test
    void shouldThrowWhenPasswordHasNoUppercase() {
        validUserDTO.setPassword("password1");
        assertThrows(ValidationException.class,
            () -> userService.createAdminUser(validUserDTO, null));
    }
    // --- TEACHER ---
    @Test
    void shouldReturnTeacherWithoutPhoto() {
        UserPlayer result = userService.createTeacherUser(validUserPlayerDTO, null);
        assertThat(result).isInstanceOf(Teacher.class);
        assertThat(result.getMail()).isEqualTo("ana@escuela.edu");
    }

    @Test
    void shouldReturnTeacherWithPhoto() {
        UserPlayer result = userService.createTeacherUser(validUserPlayerDTO, dummyImage);
        assertThat(result).isInstanceOf(Teacher.class);
    }

    @Test
    void shouldThrowWhenMailIsInvalid() {
        validUserPlayerDTO.setMail("sinArroba.edu");
        assertThrows(ValidationException.class,
            () -> userService.createTeacherUser(validUserPlayerDTO, null));
    }

    // --- FAMILIAR ---
    @Test
    void shouldReturnFamiliarWithoutPhoto() {
        UserPlayer result = userService.createFamiliarUser(validUserPlayerDTO, null);
        assertThat(result).isInstanceOf(Familiar.class);
        assertThat(result.getId()).isNotBlank();
    }

    @Test
    void shouldReturnFamiliarWithPhoto() {
        UserPlayer result = userService.createFamiliarUser(validUserPlayerDTO, dummyImage);
        assertThat(result).isInstanceOf(Familiar.class);
    }


    // --- GRADUATE ---
    @Test
    void shouldReturnGraduateWithoutPhoto() {
        UserPlayer result = userService.createGraduateUser(validUserPlayerDTO, null);
        assertThat(result).isInstanceOf(Graduate.class);
    }

    @Test
    void shouldReturnGraduateWithPhoto() {
        UserPlayer result = userService.createGraduateUser(validUserPlayerDTO, dummyImage);
        assertThat(result).isInstanceOf(Graduate.class);
    }

    @Test
    void shouldThrowWhenNameIsNullPlayerDTO() {
        validUserPlayerDTO.setName(null);
        assertThrows(ValidationException.class,
            () -> userService.createGraduateUser(validUserPlayerDTO, null));
    }
}
