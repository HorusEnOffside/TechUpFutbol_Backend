package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.*;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
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

    @Mock private PlayerRepository playerRepository;
    @Mock private UserPlayerRepository userPlayerRepository;
    @Mock private UserService userService;

    @InjectMocks private PlayerServiceImpl playerService;

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

    // ── CreateSportsProfileStudent ───────────────────────────────────────

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
            dto.setCareer(Career.ENGINEERING);
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
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

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
            assertNotNull(playerService.createSportsProfileStudent(dto, null));
        }
    }

    // ── CreateSportsProfileTeacher ───────────────────────────────────────

    @Nested
    class CreateSportsProfileTeacher {

        private TeacherPlayerDTO buildDTO() {
            TeacherPlayerDTO dto = new TeacherPlayerDTO();
            dto.setName("Ana Lopez");
            dto.setMail("ana@test.com");
            dto.setDateOfBirth(LocalDate.of(1990, 5, 15));
            dto.setGender(Gender.FEMALE);
            dto.setPassword("Password1");
            dto.setCareer(Career.ENGINEERING);
            dto.setDorsalNumber(7);
            dto.setPosition(Position.MIDFIELDER);
            return dto;
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            TeacherPlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(TeacherUserDTO.class), any())).thenReturn(mockUserPlayer);
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
            TeacherPlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verifyNoInteractions(userService);
        }

        @Test
        void whenUserNotFoundAfterCreation_thenThrowsInvalidInputException() {
            TeacherPlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.empty());
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verify(playerRepository, never()).save(any());
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            TeacherPlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createTeacherUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileTeacher(dto, null));
            verify(playerRepository, never()).save(any());
        }
    }

    // ── CreateSportsProfileFamiliar ──────────────────────────────────────

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
            when(userService.createFamiliarUser(any(UserPlayerDTO.class), any())).thenReturn(mockUserPlayer);
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
            when(userService.createFamiliarUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileFamiliar(dto, null));
        }
    }

    // ── CreateSportsProfileGraduate ──────────────────────────────────────

    @Nested
    class CreateSportsProfileGraduate {

        private GraduatePlayerDTO buildDTO() {
            GraduatePlayerDTO dto = new GraduatePlayerDTO();
            dto.setName("Laura Mora");
            dto.setMail("laura@test.com");
            dto.setDateOfBirth(LocalDate.of(1995, 7, 10));
            dto.setGender(Gender.FEMALE);
            dto.setPassword("Password1");
            dto.setCareer(Career.DATA_SCIENCE);
            dto.setDorsalNumber(3);
            dto.setPosition(Position.GOALKEEPER);
            return dto;
        }

        @Test
        void whenValidDTO_thenCreatesPlayerSuccessfully() {
            GraduatePlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createGraduateUser(any(GraduateUserDTO.class), any())).thenReturn(mockUserPlayer);
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
            GraduatePlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(true);
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileGraduate(dto, null));
        }

        @Test
        void whenSportsProfileAlreadyExists_thenThrowsInvalidInputException() {
            GraduatePlayerDTO dto = buildDTO();
            when(userPlayerRepository.existsByMailIgnoreCase(dto.getMail())).thenReturn(false);
            when(userService.createGraduateUser(any(), any())).thenReturn(mockUserPlayer);
            when(userPlayerRepository.findById("user-1")).thenReturn(Optional.of(mockUserPlayerEntity));
            when(playerRepository.existsByUserId("user-1")).thenReturn(true);
            assertThrows(InvalidInputException.class,
                    () -> playerService.createSportsProfileGraduate(dto, null));
        }
    }

    // ── GetAllPlayers ────────────────────────────────────────────────────

    @Nested
    class GetAllPlayers {

        @Test
        void whenPlayersExist_thenReturnsAllPlayers() {
            when(playerRepository.findAll()).thenReturn(List.of(
                    buildPlayerEntity("user-1", Position.FORWARD, 10),
                    buildPlayerEntity("user-2", Position.DEFENDER, 4)));

            List<Player> result = playerService.getAllPlayers();

            assertEquals(2, result.size());
            verify(playerRepository).findAll();
        }

        @Test
        void whenNoPlayersExist_thenReturnsEmptyList() {
            when(playerRepository.findAll()).thenReturn(List.of());
            assertTrue(playerService.getAllPlayers().isEmpty());
        }
    }

    // ── GetPlayerByUserId ────────────────────────────────────────────────

    @Nested
    class GetPlayerByUserId {

        @Test
        void whenUserIdExists_thenReturnsPlayer() {
            when(playerRepository.findByUserId("user-1"))
                    .thenReturn(Optional.of(buildPlayerEntity("user-1", Position.MIDFIELDER, 8)));

            Optional<Player> result = playerService.getPlayerByUserId("user-1");

            assertTrue(result.isPresent());
            assertEquals(8, result.get().getDorsalNumber());
        }

        @Test
        void whenUserIdDoesNotExist_thenReturnsEmpty() {
            when(playerRepository.findByUserId("unknown")).thenReturn(Optional.empty());
            assertTrue(playerService.getPlayerByUserId("unknown").isEmpty());
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

    // ── SearchPlayers ────────────────────────────────────────────────────

    @Nested
    class SearchPlayers {

        private PlayerEntity buildAvailablePlayer(String id, Position position, Gender gender, String name) {
            UserPlayerEntity user = new UserPlayerEntity();
            user.setId(id);
            user.setName(name);
            user.setMail(id + "@test.com");
            user.setGender(gender);
            user.setDateOfBirth(LocalDate.of(2000, 1, 1));
            user.setPasswordHash("hash");

            PlayerEntity entity = new PlayerEntity();
            entity.setId(id);
            entity.setUser(user);
            entity.setPosition(position);
            entity.setDorsalNumber(10);
            entity.setStatus(PlayerStatus.AVAILABLE);
            return entity;
        }

        @Test
        void returnsAllAvailablePlayers_whenFiltersNull() {
            PlayerEntity p = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            var result = playerService.searchPlayers(null);

            assertEquals(1, result.size());
        }

        @Test
        void returnsEmpty_whenNoAvailablePlayers() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of());

            var filters = new PlayerSearchDTO();
            assertTrue(playerService.searchPlayers(filters).isEmpty());
        }

        @Test
        void filtersByPosition() {
            PlayerEntity forward = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            PlayerEntity defender = buildAvailablePlayer("p2", Position.DEFENDER, Gender.MALE, "Ana");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(forward, defender));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPosition(Position.FORWARD);

            var result = playerService.searchPlayers(filters);
            assertEquals(1, result.size());
            assertEquals(Position.FORWARD, result.get(0).getPosition());
        }

        @Test
        void filtersByGender() {
            PlayerEntity male = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            PlayerEntity female = buildAvailablePlayer("p2", Position.FORWARD, Gender.FEMALE, "Ana");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(male, female));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setGender(Gender.FEMALE);

            var result = playerService.searchPlayers(filters);
            assertEquals(1, result.size());
        }

        @Test
        void filtersByName_caseInsensitive() {
            PlayerEntity p1 = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan Perez");
            PlayerEntity p2 = buildAvailablePlayer("p2", Position.FORWARD, Gender.MALE, "Pedro Lopez");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p1, p2));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setName("juan");

            var result = playerService.searchPlayers(filters);
            assertEquals(1, result.size());
        }

        @Test
        void blankNameFilter_ignored() {
            PlayerEntity p = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setName("   ");

            assertEquals(1, playerService.searchPlayers(filters).size());
        }

        @Test
        void filtersByPlayerId() {
            PlayerEntity p1 = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            PlayerEntity p2 = buildAvailablePlayer("p2", Position.FORWARD, Gender.MALE, "Ana");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p1, p2));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPlayerId("p1");

            var result = playerService.searchPlayers(filters);
            assertEquals(1, result.size());
            assertEquals("p1", result.get(0).getPlayerId());
        }

        @Test
        void blankPlayerIdFilter_ignored() {
            PlayerEntity p = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPlayerId("   ");

            assertEquals(1, playerService.searchPlayers(filters).size());
        }

        @Test
        void filtersBySemester_matchesStudent() {
            StudentEntity student = new StudentEntity();
            student.setId("s1");
            student.setName("Juan");
            student.setMail("s1@test.com");
            student.setGender(Gender.MALE);
            student.setDateOfBirth(LocalDate.of(2000, 1, 1));
            student.setPasswordHash("hash");
            student.setSemester(3);

            PlayerEntity p = new PlayerEntity();
            p.setId("p1");
            p.setUser(student);
            p.setPosition(Position.FORWARD);
            p.setDorsalNumber(10);
            p.setStatus(PlayerStatus.AVAILABLE);

            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setSemester(3);

            assertEquals(1, playerService.searchPlayers(filters).size());
        }

        @Test
        void filtersBySemester_excludesNonStudent() {
            PlayerEntity p = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setSemester(3);

            assertTrue(playerService.searchPlayers(filters).isEmpty());
        }

        @Test
        void filtersByAge_matches() {
            int expectedAge = LocalDate.now().getYear() - 2000;
            PlayerEntity p = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setAge(expectedAge);

            assertEquals(1, playerService.searchPlayers(filters).size());
        }

        @Test
        void filtersByAge_excludesWhenDobNull() {
            UserPlayerEntity user = new UserPlayerEntity();
            user.setId("p1");
            user.setName("Juan");
            user.setMail("p1@test.com");
            user.setGender(Gender.MALE);
            user.setDateOfBirth(null);
            user.setPasswordHash("hash");

            PlayerEntity p = new PlayerEntity();
            p.setId("p1");
            p.setUser(user);
            p.setPosition(Position.FORWARD);
            p.setDorsalNumber(10);
            p.setStatus(PlayerStatus.AVAILABLE);

            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(p));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setAge(25);

            assertTrue(playerService.searchPlayers(filters).isEmpty());
        }

        @Test
        void multipleFilters_allMustMatch() {
            PlayerEntity match = buildAvailablePlayer("p1", Position.FORWARD, Gender.MALE, "Juan");
            PlayerEntity noMatch = buildAvailablePlayer("p2", Position.DEFENDER, Gender.FEMALE, "Ana");
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of(match, noMatch));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPosition(Position.FORWARD);
            filters.setGender(Gender.MALE);

            var result = playerService.searchPlayers(filters);
            assertEquals(1, result.size());
        }
    }

    // ── UpdateStatus ─────────────────────────────────────────────────────

    @Nested
    class UpdateStatus {

        @Test
        void cuandoJugadorExiste_actualizaEstado() {
            PlayerEntity entity = buildPlayerEntity("user-1", Position.FORWARD, 9);
            when(playerRepository.findByUserId("user-1")).thenReturn(Optional.of(entity));
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Player result = playerService.updateStatus("user-1", PlayerStatus.INJURED);

            assertNotNull(result);
            assertEquals(PlayerStatus.INJURED, entity.getStatus());
            verify(playerRepository).save(entity);
        }

        @Test
        void puedeCambiarANoDisponible() {
            PlayerEntity entity = buildPlayerEntity("user-2", Position.MIDFIELDER, 8);
            when(playerRepository.findByUserId("user-2")).thenReturn(Optional.of(entity));
            when(playerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            playerService.updateStatus("user-2", PlayerStatus.NOT_AVAILABLE);

            assertEquals(PlayerStatus.NOT_AVAILABLE, entity.getStatus());
        }

        @Test
        void cuandoJugadorNoExiste_lanzaExcepcion() {
            when(playerRepository.findByUserId("nobody")).thenReturn(Optional.empty());

            assertThrows(com.escuela.techcup.core.exception.InvalidInputException.class,
                    () -> playerService.updateStatus("nobody", PlayerStatus.AVAILABLE));

            verify(playerRepository, never()).save(any());
        }

        @Test
        void cuandoUserIdEsNulo_lanzaExcepcion() {
            assertThrows(com.escuela.techcup.core.exception.InvalidInputException.class,
                    () -> playerService.updateStatus(null, PlayerStatus.AVAILABLE));
        }

        @Test
        void cuandoStatusEsNulo_lanzaExcepcion() {
            assertThrows(com.escuela.techcup.core.exception.InvalidInputException.class,
                    () -> playerService.updateStatus("user-1", null));
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

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
        return entity;
    }
}