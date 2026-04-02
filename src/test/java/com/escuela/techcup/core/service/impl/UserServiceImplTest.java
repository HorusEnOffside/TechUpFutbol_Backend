package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.*;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.repository.users.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private AdministratorRepository administratorRepository;
    @Mock private OrganizerRepository organizerRepository;
    @Mock private RefereeRepository refereeRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TeacherRepository teacherRepository;
    @Mock private FamiliarRepository familiarRepository;
    @Mock private GraduateRepository graduateRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDTO userDTO;
    private UserPlayerDTO userPlayerDTO;
    private StudentUserDTO studentUserDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");

        userPlayerDTO = new UserPlayerDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");

        studentUserDTO = new StudentUserDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1", 3);
    }

    // --- createAdminUser ---

    @Test
    void createAdminUser_returnsAdminWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(administratorRepository.save(any())).thenReturn(new AdministratorEntity());

        User result = userService.createAdminUser(userDTO, null);

        assertNotNull(result);
        verify(administratorRepository).save(any());
    }

    @Test
    void createAdminUser_throwsWhenMailAlreadyExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createAdminUser(userDTO, null));
    }

    // --- createOrganizerUser ---

    @Test
    void createOrganizerUser_returnsOrganizerWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(organizerRepository.save(any())).thenReturn(new OrganizerEntity());

        User result = userService.createOrganizerUser(userDTO, null);

        assertNotNull(result);
        verify(organizerRepository).save(any());
    }

    // --- createRefereeUser ---

    @Test
    void createRefereeUser_returnsRefereeWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(refereeRepository.save(any())).thenReturn(new RefereeEntity());

        User result = userService.createRefereeUser(userDTO, null);

        assertNotNull(result);
        verify(refereeRepository).save(any());
    }

    // --- createTeacherUser ---

    @Test
    void createTeacherUser_returnsTeacherWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(teacherRepository.save(any())).thenReturn(new TeacherEntity());

        UserPlayer result = userService.createTeacherUser(userPlayerDTO, null);

        assertNotNull(result);
        verify(teacherRepository).save(any());
    }

    // --- createFamiliarUser ---

    @Test
    void createFamiliarUser_returnsFamiliarWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(familiarRepository.save(any())).thenReturn(new FamiliarEntity());

        UserPlayer result = userService.createFamiliarUser(userPlayerDTO, null);

        assertNotNull(result);
        verify(familiarRepository).save(any());
    }

    // --- createGraduateUser ---

    @Test
    void createGraduateUser_returnsGraduateWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(graduateRepository.save(any())).thenReturn(new GraduateEntity());

        UserPlayer result = userService.createGraduateUser(userPlayerDTO, null);

        assertNotNull(result);
        verify(graduateRepository).save(any());
    }

    // --- createStudentUser ---

    @Test
    void createStudentUser_returnsStudentWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(studentRepository.save(any())).thenReturn(new StudentEntity());

        UserPlayer result = userService.createStudentUser(studentUserDTO, null);

        assertNotNull(result);
        verify(studentRepository).save(any());
    }

    @Test
    void createStudentUser_throwsWhenMailAlreadyExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createStudentUser(studentUserDTO, null));
    }

    // --- getAllUsers ---

    @Test
    void getAllUsers_returnsEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        assertTrue(userService.getAllUsers().isEmpty());
    }

    // --- getUserById ---

    @Test
    void getUserById_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> userService.getUserById(null));
    }

    @Test
    void getUserById_throwsWhenIdIsBlank() {
        assertThrows(InvalidInputException.class,
                () -> userService.getUserById("  "));
    }

    @Test
    void getUserById_returnsEmptyWhenNotFound() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertTrue(userService.getUserById("no-existe").isEmpty());
    }

    // --- getUserByMail ---

    @Test
    void getUserByMail_throwsWhenMailIsNull() {
        assertThrows(InvalidInputException.class,
                () -> userService.getUserByMail(null));
    }

    @Test
    void getUserByMail_throwsWhenMailIsBlank() {
        assertThrows(InvalidInputException.class,
                () -> userService.getUserByMail("  "));
    }

    @Test
    void getUserByMail_returnsEmptyWhenNotFound() {
        when(userRepository.findByMailIgnoreCase("noexiste@test.com")).thenReturn(Optional.empty());

        assertTrue(userService.getUserByMail("noexiste@test.com").isEmpty());
    }

    @Test
    void createAdminUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createAdminUser(null, null));
    }

    @Test
    void createOrganizerUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createOrganizerUser(null, null));
    }

    @Test
    void createRefereeUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createRefereeUser(null, null));
    }

    @Test
    void createTeacherUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createTeacherUser(null, null));
    }

    @Test
    void createFamiliarUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createFamiliarUser(null, null));
    }

    @Test
    void createGraduateUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createGraduateUser(null, null));
    }

    @Test
    void createStudentUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class,
                () -> userService.createStudentUser(null, null));
    }
}