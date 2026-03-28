package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserPlayerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;
    

    @Mock
    private UserService userService;

    @Mock
    private UserPlayerRepository userPlayerRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;
    @BeforeEach
    void setUp() {
        when(playerRepository.existsByUserId(anyString())).thenReturn(false);
    }
    @Test
    void createSportsProfileStudent() {
        // Arrange
        StudentPlayerDTO dto = new StudentPlayerDTO();
        dto.setName("Juan Pérez");
        dto.setMail("juan@escuela.edu");
        dto.setDateOfBirth(LocalDate.of(2000, 6, 15));
        dto.setGender(Gender.MALE);
        dto.setPassword("Password1");
        dto.setSemester(4);
        dto.setDorsalNumber(10);
        dto.setPosition(Position.FORWARD);
        UserPlayer userPlayer = new UserPlayer("user-id", "Juan Pérez", "juan@escuela.edu", LocalDate.of(2000, 6, 15), Gender.MALE, "Password1");
        UserPlayerEntity mockUserPlayerEntity = mock(UserPlayerEntity.class);
        when(mockUserPlayerEntity.getId()).thenReturn("mock-id");

        lenient().doReturn(userPlayer).when(userService).createStudentUser(any(StudentUserDTO.class), isNull());


        when(userPlayerRepository.findById(anyString()))
            .thenReturn(Optional.of(mockUserPlayerEntity));

        // Act
        Player result = playerService.createSportsProfileStudent(dto, null);

        // Assert
        assertThat(result).isNotNull();
        verify(userService, times(1)).createStudentUser(any(StudentUserDTO.class), isNull());
    }
}
