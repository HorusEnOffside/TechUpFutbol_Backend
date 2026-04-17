package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.*;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.*;
import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.repository.users.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
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

    @InjectMocks private UserServiceImpl userService;

    private UserDTO userDTO;
    private UserPlayerDTO userPlayerDTO;
    private StudentUserDTO studentUserDTO;
    private TeacherUserDTO teacherUserDTO;
    private GraduateUserDTO graduateUserDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");

        userPlayerDTO = new UserPlayerDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");

        studentUserDTO = new StudentUserDTO("Juan", "juan@escuela.edu.co",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1", 3, Career.ENGINEERING);

        teacherUserDTO = new TeacherUserDTO("Luis", "luis@escuela.edu.co",
                LocalDate.of(1985, 3, 10), Gender.MALE, "Password1", Career.DATA_SCIENCE);

        graduateUserDTO = new GraduateUserDTO("Ana", "ana@escuela.edu.co",
                LocalDate.of(1998, 6, 5), Gender.FEMALE, "Password1", Career.ENGINEERING);
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

    @Test
    void createAdminUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createAdminUser(null, null));
    }

    @Test
    void createAdminUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(administratorRepository.save(any())).thenReturn(new AdministratorEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        User result = userService.createAdminUser(userDTO, img);

        assertNotNull(result);
        verify(administratorRepository).save(any());
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

    @Test
    void createOrganizerUser_throwsWhenMailExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createOrganizerUser(userDTO, null));
    }

    @Test
    void createOrganizerUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createOrganizerUser(null, null));
    }

    @Test
    void createOrganizerUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(organizerRepository.save(any())).thenReturn(new OrganizerEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createOrganizerUser(userDTO, img));
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

    @Test
    void createRefereeUser_throwsWhenMailExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createRefereeUser(userDTO, null));
    }

    @Test
    void createRefereeUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createRefereeUser(null, null));
    }

    @Test
    void createRefereeUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(refereeRepository.save(any())).thenReturn(new RefereeEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createRefereeUser(userDTO, img));
    }

    // --- createTeacherUser ---

    @Test
    void createTeacherUser_returnsTeacherWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(teacherRepository.save(any())).thenReturn(new TeacherEntity());

        UserPlayer result = userService.createTeacherUser(teacherUserDTO, null);

        assertNotNull(result);
        verify(teacherRepository).save(any());
    }

    @Test
    void createTeacherUser_throwsWhenMailExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createTeacherUser(teacherUserDTO, null));
    }

    @Test
    void createTeacherUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createTeacherUser(null, null));
    }

    @Test
    void createTeacherUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(teacherRepository.save(any())).thenReturn(new TeacherEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createTeacherUser(teacherUserDTO, img));
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

    @Test
    void createFamiliarUser_throwsWhenMailExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createFamiliarUser(userPlayerDTO, null));
    }

    @Test
    void createFamiliarUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createFamiliarUser(null, null));
    }

    @Test
    void createFamiliarUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(familiarRepository.save(any())).thenReturn(new FamiliarEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createFamiliarUser(userPlayerDTO, img));
    }

    // --- createGraduateUser ---

    @Test
    void createGraduateUser_returnsGraduateWhenValid() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(graduateRepository.save(any())).thenReturn(new GraduateEntity());

        UserPlayer result = userService.createGraduateUser(graduateUserDTO, null);

        assertNotNull(result);
        verify(graduateRepository).save(any());
    }

    @Test
    void createGraduateUser_throwsWhenMailExists() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(true);

        assertThrows(InvalidInputException.class,
                () -> userService.createGraduateUser(graduateUserDTO, null));
    }

    @Test
    void createGraduateUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createGraduateUser(null, null));
    }

    @Test
    void createGraduateUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(graduateRepository.save(any())).thenReturn(new GraduateEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createGraduateUser(graduateUserDTO, img));
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

    @Test
    void createStudentUser_throwsWhenDtoIsNull() {
        assertThrows(Exception.class, () -> userService.createStudentUser(null, null));
    }

    @Test
    void createStudentUser_withProfilePicture_savesCorrectly() {
        when(userRepository.existsByMailIgnoreCase(anyString())).thenReturn(false);
        when(studentRepository.save(any())).thenReturn(new StudentEntity());

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        assertNotNull(userService.createStudentUser(studentUserDTO, img));
    }

    // --- getAllUsers ---

    @Test
    void getAllUsers_returnsEmptyListWhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());
        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void getAllUsers_returnsMappedUsers() {
        AdministratorEntity entity = new AdministratorEntity();
        entity.setId("u1");
        entity.setName("Juan");
        entity.setMail("juan@test.com");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hash");

        when(userRepository.findAll()).thenReturn(List.of(entity));

        List<User> result = userService.getAllUsers();
        assertFalse(result.isEmpty());
    }

    // --- getUserById ---

    @Test
    void getUserById_throwsWhenIdIsNull() {
        assertThrows(InvalidInputException.class, () -> userService.getUserById(null));
    }

    @Test
    void getUserById_throwsWhenIdIsBlank() {
        assertThrows(InvalidInputException.class, () -> userService.getUserById("  "));
    }

    @Test
    void getUserById_returnsEmptyWhenNotFound() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());
        assertTrue(userService.getUserById("no-existe").isEmpty());
    }

    @Test
    void getUserById_returnsPresentWhenFound() {
        AdministratorEntity entity = new AdministratorEntity();
        entity.setId("u1");
        entity.setName("Juan");
        entity.setMail("juan@test.com");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hash");

        when(userRepository.findById("u1")).thenReturn(Optional.of(entity));
        assertTrue(userService.getUserById("u1").isPresent());
    }

    // --- getUserByMail ---

    @Test
    void getUserByMail_throwsWhenMailIsNull() {
        assertThrows(InvalidInputException.class, () -> userService.getUserByMail(null));
    }

    @Test
    void getUserByMail_throwsWhenMailIsBlank() {
        assertThrows(InvalidInputException.class, () -> userService.getUserByMail("  "));
    }

    @Test
    void getUserByMail_returnsEmptyWhenNotFound() {
        when(userRepository.findByMailIgnoreCase("noexiste@test.com")).thenReturn(Optional.empty());
        assertTrue(userService.getUserByMail("noexiste@test.com").isEmpty());
    }

    @Test
    void getUserByMail_returnsPresentWhenFound() {
        AdministratorEntity entity = new AdministratorEntity();
        entity.setId("u1");
        entity.setName("Juan");
        entity.setMail("juan@test.com");
        entity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        entity.setGender(Gender.MALE);
        entity.setPasswordHash("hash");

        when(userRepository.findByMailIgnoreCase("juan@test.com")).thenReturn(Optional.of(entity));
        assertTrue(userService.getUserByMail("juan@test.com").isPresent());
    }
}