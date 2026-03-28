package com.escuela.techcup.core.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.escuela.techcup.persistence.entity.users.AdministratorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.UserNotFoundException;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.persistence.entity.users.UserEntity;
import com.escuela.techcup.persistence.repository.users.UserRepository;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UserEntity mockUserEntity;

    @BeforeEach
    void setUp() {
        mockUserEntity = new AdministratorEntity();
        mockUserEntity.setId("user-001");
        mockUserEntity.setName("Carlos");
        mockUserEntity.setMail("carlos@escuela.edu.co");
        mockUserEntity.setGender(Gender.MALE);
        mockUserEntity.setRoles(new java.util.HashSet<>(
                java.util.Set.of(UserRole.BASEUSER)
        ));
    }

    @Test
    void assignRole_shouldAssignRoleSuccessfully() {
        when(userRepository.findById("user-001")).thenReturn(Optional.of(mockUserEntity));
        when(userRepository.save(any())).thenReturn(mockUserEntity);

        User result = adminService.assignRole("user-001", UserRole.ADMIN);

        assertNotNull(result);
        verify(userRepository).save(mockUserEntity);
    }

    @Test
    void assignRole_shouldThrowWhenUserIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> adminService.assignRole(null, UserRole.ADMIN));
    }

    @Test
    void assignRole_shouldThrowWhenUserIdIsBlank() {
        assertThrows(InvalidInputException.class,
                () -> adminService.assignRole("  ", UserRole.ADMIN));
    }

    @Test
    void assignRole_shouldThrowWhenRoleIsNull() {
        assertThrows(InvalidInputException.class,
                () -> adminService.assignRole("user-001", null));
    }

    @Test
    void assignRole_shouldThrowWhenUserNotFound() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> adminService.assignRole("no-existe", UserRole.ADMIN));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(mockUserEntity));

        List<User> result = adminService.getAllUsers();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getUserById_shouldReturnUserWhenExists() {
        when(userRepository.findById("user-001")).thenReturn(Optional.of(mockUserEntity));

        Optional<User> result = adminService.getUserById("user-001");

        assertTrue(result.isPresent());
    }

    @Test
    void getUserById_shouldReturnEmptyWhenNotFound() {
        when(userRepository.findById("no-existe")).thenReturn(Optional.empty());

        Optional<User> result = adminService.getUserById("no-existe");

        assertTrue(result.isEmpty());
    }

    @Test
    void getUserById_shouldThrowWhenIdIsNull() {
        assertThrows(InvalidInputException.class,
                () -> adminService.getUserById(null));
    }
}