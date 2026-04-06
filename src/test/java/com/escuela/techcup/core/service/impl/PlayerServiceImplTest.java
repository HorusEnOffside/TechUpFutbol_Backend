package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserPlayerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private UserPlayerRepository userPlayerRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private UserPlayer mockUserPlayer;
    private UserPlayerEntity mockUserPlayerEntity;
    private BufferedImage mockImage;

    @BeforeEach
    void setUp() {
        mockUserPlayer = new UserPlayer("user-1", "Juan Perez", "juan@test.com",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");

        mockUserPlayerEntity = new UserPlayerEntity();
        mockUserPlayerEntity.setId("user-1");

        mockImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
    }


    // -------------------------------------------------------------------------
    // createSportsProfileStudent
    // -------------------------------------------------------------------------

    @Nested
    class CreateSportsProfileStudent {

        private StudentPlayerDTO buildDTO() {
            StudentPlayerDTO dto = new StudentPlayerDTO();
            dto.setName("Juan Perez");
            dto.setMail("juan@test.com");
            dto.setDateOfBirth(LocalDate.of(2000, 1, 1));
            dto.setGender(Gender.MALE);
            dto.setPassword("Password1");
            dto.setSemester(3);
            dto.setDorsalNumber(10);
            dto.setPosition(Position.FORWARD);
            return dto;
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            StudentPlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createStudentUser(any(StudentUserDTO.class), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(false);
            when(playerRepository.save(any(PlayerEntity.class))).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.createSportsProfileStudent(dto, mockImage);

            assertNotNull(result);
            assertEquals(Position.FORWARD, result.getPosition());
            assertEquals(10, result.getDorsalNumber());
            assertEquals(PlayerStatus.AVAILABLE, result.getStatus());
            verify(playerRepository).save(any(PlayerEntity.class));
        }

        @Test
        void whenDTOIsNull_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileStudent(null, null));
            verifyNoInteractions(userService, playerRepository);
        }

        @Test
        void whenDorsalNumberIsZero_thenThrowsValidationException() {
            StudentPlayerDTO dto = buildDTO();
            dto.setDorsalNumber(0);

            assertThrows(Exception.class,
                    () -> playerService.createSportsProfileStudent(dto, null));
            verifyNoInteractions(userService, playerRepository);
        }

        @Test
        void whenMailAlreadyExists_thenThrowsInvalidInputException() {
            StudentPlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileStudent(dto, null));
            verifyNoInteractions(userService);
        }

        @Test
        void whenUserNotFoundAfterCreation_thenThrowsInvalidInputException() {
            StudentPlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createStudentUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.empty());

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileStudent(dto, null));
            verify(playerRepository, never()).save(any());
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            StudentPlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createStudentUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileStudent(dto, null));
            verify(playerRepository, never()).save(any());
        }

        @Test
        void whenNullProfilePicture_thenCreatesPlayerSuccessfully() {
            StudentPlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createStudentUser(any(), isNull())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(false);
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.createSportsProfileStudent(dto, null);

            assertNotNull(result);
        }
    }


    // -------------------------------------------------------------------------
    // createSportsProfileTeacher
    // -------------------------------------------------------------------------
    /**
    @Nested
    class CreateSportsProfileTeacher {

        private PlayerDTO buildDTO() {
            return new PlayerDTO("Ana Lopez", "ana@test.com",
                    LocalDate.of(1990, 5, 15), Gender.FEMALE, "Password1", 7, Position.MIDFIELDER);
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(UserPlayerDTO.class), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(false);
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.createSportsProfileTeacher(dto, mockImage);

            assertNotNull(result);
            assertEquals(Position.MIDFIELDER, result.getPosition());
            assertEquals(7, result.getDorsalNumber());
            verify(playerRepository).save(any(PlayerEntity.class));
        }

        @Test
        void whenDTOIsNull_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(null, null));
            verifyNoInteractions(userService, playerRepository);
        }

        @Test
        void whenMailAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verifyNoInteractions(userService);
        }

        @Test
        void whenUserNotFoundAfterCreation_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.empty());

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verify(playerRepository, never()).save(any());
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verify(playerRepository, never()).save(any());
        }
    }


    // -------------------------------------------------------------------------
    // createSportsProfileFamiliar
    // -------------------------------------------------------------------------

    @Nested
    class CreateSportsProfileFamiliar {

        private PlayerDTO buildDTO() {
            return new PlayerDTO("Carlos Ruiz", "carlos@test.com",
                    LocalDate.of(1985, 3, 20), Gender.MALE, "Password1", 5, Position.DEFENDER);
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(false);
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.createSportsProfileFamiliar(dto, null);

            assertNotNull(result);
            assertEquals(Position.DEFENDER, result.getPosition());
            verify(playerRepository).save(any(PlayerEntity.class));
        }

        @Test
        void whenDTOIsNull_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileFamiliar(null, null));
        }

        @Test
        void whenMailAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileFamiliar(dto, null));
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileFamiliar(dto, null));
        }
    }


    // -------------------------------------------------------------------------
    // createSportsProfileGraduate
    // -------------------------------------------------------------------------

    @Nested
    class CreateSportsProfileGraduate {

        private PlayerDTO buildDTO() {
            return new PlayerDTO("Laura Mora", "laura@test.com",
                    LocalDate.of(1995, 7, 10), Gender.FEMALE, "Password1", 3, Position.GOALKEEPER);
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(false);
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.createSportsProfileGraduate(dto, mockImage);

            assertNotNull(result);
            assertEquals(Position.GOALKEEPER, result.getPosition());
            verify(playerRepository).save(any(PlayerEntity.class));
        }

        @Test
        void whenDTOIsNull_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileGraduate(null, null));
        }

        @Test
        void whenMailAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileGraduate(dto, null));
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            PlayerDTO dto = buildDTO();

            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);

            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileGraduate(dto, null));
        }
    }


    // -------------------------------------------------------------------------
    // getAllPlayers
    // -------------------------------------------------------------------------

    @Nested
    class GetAllPlayers {

        @Test
        void whenPlayersExist_thenReturnsAllPlayers() {
            PlayerEntity entity1 = buildPlayerEntity("user-1", Position.FORWARD, 10);
            PlayerEntity entity2 = buildPlayerEntity("user-2", Position.DEFENDER, 4);

            when(playerRepository.findAll()).thenReturn(List.of(entity1, entity2));

            List<Player> result = playerService.getAllPlayers();

            assertEquals(2, result.size());
            verify(playerRepository).findAll();
        }

        @Test
        void whenNoPlayersExist_thenReturnsEmptyList() {
            when(playerRepository.findAll()).thenReturn(List.of());

            List<Player> result = playerService.getAllPlayers();

            assertTrue(result.isEmpty());
        }
    }


    // -------------------------------------------------------------------------
    // getPlayerByUserId
    // -------------------------------------------------------------------------

    @Nested
    class GetPlayerByUserId {

        @Test
        void whenUserIdExists_thenReturnsPlayer() {
            PlayerEntity entity = buildPlayerEntity("user-1", Position.MIDFIELDER, 8);

            when(playerRepository.findByUserId("user-1")).thenReturn(Optional.of(entity));

            Optional<Player> result = playerService.getPlayerByUserId("user-1");

            assertTrue(result.isPresent());
            assertEquals(8, result.get().getDorsalNumber());
        }

        @Test
        void whenUserIdDoesNotExist_thenReturnsEmpty() {
            when(playerRepository.findByUserId("unknown")).thenReturn(Optional.empty());

            Optional<Player> result = playerService.getPlayerByUserId("unknown");

            assertTrue(result.isEmpty());
        }

        @Test
        void whenUserIdIsNull_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.getPlayerByUserId(null));
            verifyNoInteractions(playerRepository);
        }

        @Test
        void whenUserIdIsBlank_thenThrowsInvalidInputException() {
            assertThrows(InvalidInputException.class,
                    () -> playerService.getPlayerByUserId("   "));
            verifyNoInteractions(playerRepository);
        }
    }


    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private PlayerEntity buildPlayerEntity(String userId, Position position, int dorsal) {
        UserPlayerEntity userPlayerEntity = new UserPlayerEntity();
        userPlayerEntity.setId(userId);
        userPlayerEntity.setName("Test User");
        userPlayerEntity.setMail(userId + "@test.com");
        userPlayerEntity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userPlayerEntity.setGender(Gender.MALE);
        userPlayerEntity.setPasswordHash("Password1");

        PlayerEntity entity = new PlayerEntity();
        entity.setId(userId);
        entity.setUser(userPlayerEntity);
        entity.setPosition(position);
        entity.setDorsalNumber(dorsal);
        entity.setStatus(PlayerStatus.AVAILABLE);
        entity.setStatus(PlayerStatus.AVAILABLE);
        return entity;
    }
     **/
}
