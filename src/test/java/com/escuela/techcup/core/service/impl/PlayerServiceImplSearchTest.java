package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PlayerSearchDTO;
import com.escuela.techcup.controller.dto.PlayerSearchResultDTO;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplSearchTest {

    @Mock private PlayerRepository playerRepository;
    @Mock private com.escuela.techcup.persistence.repository.users.UserPlayerRepository userPlayerRepository;
    @Mock private com.escuela.techcup.core.service.UserService userService;

    @InjectMocks private PlayerServiceImpl service;

    private PlayerEntity forwardMale;
    private PlayerEntity goalkeeperFemale;
    private PlayerEntity studentPlayer;

    @BeforeEach
    void setUp() {
        forwardMale = buildPlayer("p-1", "Carlos Torres", "carlos@test.com",
                Position.FORWARD, 9, Gender.MALE,
                LocalDate.of(2000, 5, 15), null);

        goalkeeperFemale = buildPlayer("p-2", "Ana Ruiz", "ana@test.com",
                Position.GOALKEEPER, 1, Gender.FEMALE,
                LocalDate.of(1998, 3, 20), null);

        StudentEntity studentUser = new StudentEntity();
        studentUser.setId("user-3");
        studentUser.setName("Luis Mora");
        studentUser.setMail("luis@test.com");
        studentUser.setDateOfBirth(LocalDate.of(2002, 1, 1));
        studentUser.setGender(Gender.MALE);
        studentUser.setPasswordHash("hash");
        studentUser.setSemester(4);

        studentPlayer = new PlayerEntity();
        studentPlayer.setId("p-3");
        studentPlayer.setUser(studentUser);
        studentPlayer.setPosition(Position.MIDFIELDER);
        studentPlayer.setDorsalNumber(8);
        studentPlayer.setStatus(PlayerStatus.AVAILABLE);
    }

    // ── Sin filtros ──────────────────────────────────────────────────────

    @Nested
    class NoFilters {

        @Test
        void returnsAllAvailablePlayers() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            List<PlayerSearchResultDTO> result = service.searchPlayers(new PlayerSearchDTO());

            assertThat(result).hasSize(2);
        }

        @Test
        void returnsEmptyWhenNoPlayersAvailable() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE)).thenReturn(List.of());

            List<PlayerSearchResultDTO> result = service.searchPlayers(new PlayerSearchDTO());

            assertThat(result).isEmpty();
        }

        @Test
        void withNullFilters_returnsAllAvailable() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            List<PlayerSearchResultDTO> result = service.searchPlayers(null);

            assertThat(result).hasSize(1);
        }
    }

    // ── Filtro por posición ──────────────────────────────────────────────

    @Nested
    class FilterByPosition {

        @Test
        void returnsOnlyMatchingPosition() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPosition(Position.FORWARD);

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPosition()).isEqualTo(Position.FORWARD);
        }

        @Test
        void returnsEmptyWhenNoMatchingPosition() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPosition(Position.DEFENDER);

            assertThat(service.searchPlayers(filters)).isEmpty();
        }
    }

    // ── Filtro por género ────────────────────────────────────────────────

    @Nested
    class FilterByGender {

        @Test
        void returnsOnlyFemale() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setGender(Gender.FEMALE);

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getGender()).isEqualTo(Gender.FEMALE);
        }
    }

    // ── Filtro por nombre ────────────────────────────────────────────────

    @Nested
    class FilterByName {

        @Test
        void findsByPartialNameCaseInsensitive() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setName("carlos");

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Carlos Torres");
        }

        @Test
        void returnsEmptyWhenNameNotFound() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setName("xyz");

            assertThat(service.searchPlayers(filters)).isEmpty();
        }

        @Test
        void blankNameIgnoresFilter() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setName("   ");

            assertThat(service.searchPlayers(filters)).hasSize(2);
        }
    }

    // ── Filtro por ID ────────────────────────────────────────────────────

    @Nested
    class FilterByPlayerId {

        @Test
        void findsByExactId() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPlayerId("p-2");

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPlayerId()).isEqualTo("p-2");
        }

        @Test
        void returnsEmptyWhenIdNotFound() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setPlayerId("no-existe");

            assertThat(service.searchPlayers(filters)).isEmpty();
        }
    }

    // ── Filtro por semestre ──────────────────────────────────────────────

    @Nested
    class FilterBySemester {

        @Test
        void returnsStudentWithMatchingSemester() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(studentPlayer, forwardMale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setSemester(4);

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPlayerId()).isEqualTo("p-3");
        }

        @Test
        void excludesNonStudentsWhenSemesterFilter() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setSemester(3);

            assertThat(service.searchPlayers(filters)).isEmpty();
        }

        @Test
        void excludesStudentWithDifferentSemester() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(studentPlayer));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setSemester(7);

            assertThat(service.searchPlayers(filters)).isEmpty();
        }
    }

    // ── Filtro por edad ──────────────────────────────────────────────────

    @Nested
    class FilterByAge {

        @Test
        void returnsPlayerWithMatchingAge() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale, goalkeeperFemale));

            int expectedAge = LocalDate.now().getYear() - 2000;

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setAge(expectedAge);

            List<PlayerSearchResultDTO> result = service.searchPlayers(filters);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).isEqualTo("Carlos Torres");
        }

        @Test
        void excludesPlayerWithNullDateOfBirth() {
            UserPlayerEntity userNoDate = new UserPlayerEntity();
            userNoDate.setId("user-x");
            userNoDate.setName("Sin Fecha");
            userNoDate.setMail("x@test.com");
            userNoDate.setGender(Gender.MALE);
            userNoDate.setPasswordHash("hash");
            // dateOfBirth null a propósito

            PlayerEntity noDatePlayer = new PlayerEntity();
            noDatePlayer.setId("p-x");
            noDatePlayer.setUser(userNoDate);
            noDatePlayer.setPosition(Position.FORWARD);
            noDatePlayer.setDorsalNumber(5);
            noDatePlayer.setStatus(PlayerStatus.AVAILABLE);

            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(noDatePlayer));

            PlayerSearchDTO filters = new PlayerSearchDTO();
            filters.setAge(25);

            assertThat(service.searchPlayers(filters)).isEmpty();
        }
    }

    // ── Resultado incluye equipo ─────────────────────────────────────────

    @Nested
    class TeamInfo {

        @Test
        void includesTeamNameWhenPlayerHasTeam() {
            TeamEntity team = new TeamEntity();
            team.setId("team-1");
            team.setName("Los Tigres");
            forwardMale.setTeam(team);

            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            List<PlayerSearchResultDTO> result = service.searchPlayers(new PlayerSearchDTO());

            assertThat(result.get(0).getTeamName()).isEqualTo("Los Tigres");
        }

        @Test
        void teamNameIsNullWhenPlayerHasNoTeam() {
            when(playerRepository.findByStatus(PlayerStatus.AVAILABLE))
                    .thenReturn(List.of(forwardMale));

            List<PlayerSearchResultDTO> result = service.searchPlayers(new PlayerSearchDTO());

            assertThat(result.get(0).getTeamName()).isNull();
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private PlayerEntity buildPlayer(String id, String name, String mail,
                                     Position position, int dorsal,
                                     Gender gender, LocalDate dob, TeamEntity team) {
        UserPlayerEntity user = new UserPlayerEntity();
        user.setId("user-" + id);
        user.setName(name);
        user.setMail(mail);
        user.setGender(gender);
        user.setDateOfBirth(dob);
        user.setPasswordHash("hash");

        PlayerEntity player = new PlayerEntity();
        player.setId(id);
        player.setUser(user);
        player.setPosition(position);
        player.setDorsalNumber(dorsal);
        player.setStatus(PlayerStatus.AVAILABLE);
        player.setTeam(team);
        return player;
    }
}