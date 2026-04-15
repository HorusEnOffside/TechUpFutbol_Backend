package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserPlayerRepository;
import com.escuela.techcup.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Tests para findByNameContaining en PlayerServiceImpl y TeamServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class SancionServiceSearchTest {

    // ── PlayerServiceImpl.findByNameContaining ───────────────────────────

    @Nested
    class PlayerFindByName {

        @Mock private PlayerRepository playerRepository;
        @Mock private UserPlayerRepository userPlayerRepository;
        @Mock private UserService userService;
        @InjectMocks private PlayerServiceImpl playerService;

        private PlayerEntity buildPlayerEntity(String id, String name) {
            UserPlayerEntity user = new UserPlayerEntity();
            user.setId("user-" + id);
            user.setName(name);
            user.setMail(name.toLowerCase().replace(" ", "") + "@test.com");
            user.setGender(Gender.MALE);
            user.setDateOfBirth(LocalDate.of(2000, 1, 1));
            user.setPasswordHash("hash");

            PlayerEntity player = new PlayerEntity();
            player.setId(id);
            player.setUser(user);
            player.setPosition(Position.FORWARD);
            player.setDorsalNumber(9);
            player.setStatus(PlayerStatus.AVAILABLE);
            return player;
        }

        @Test
        void retornaJugadorCuandoExiste() {
            PlayerEntity entity = buildPlayerEntity("p-1", "Carlos Lopez");
            when(playerRepository.findByUser_NameContainingIgnoreCase("Carlos"))
                    .thenReturn(List.of(entity));

            Optional<Player> result = playerService.findByNameContaining("Carlos");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Carlos Lopez");
            assertThat(result.get().getUserId()).isEqualTo("user-p-1");
        }

        @Test
        void retornaEmptyWhenNotFound() {
            when(playerRepository.findByUser_NameContainingIgnoreCase("xyz"))
                    .thenReturn(List.of());

            Optional<Player> result = playerService.findByNameContaining("xyz");

            assertThat(result).isEmpty();
        }

        @Test
        void retornaPrimerResultadoCuandoHayVariosMatches() {
            PlayerEntity p1 = buildPlayerEntity("p-1", "Carlos A");
            PlayerEntity p2 = buildPlayerEntity("p-2", "Carlos B");
            when(playerRepository.findByUser_NameContainingIgnoreCase("Carlos"))
                    .thenReturn(List.of(p1, p2));

            Optional<Player> result = playerService.findByNameContaining("Carlos");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Carlos A");
        }

        @Test
        void lanzaExcepcionCuandoNombreEsBlank() {
            assertThatThrownBy(() -> playerService.findByNameContaining("  "))
                    .isInstanceOf(InvalidInputException.class);
            verifyNoInteractions(playerRepository);
        }

        @Test
        void lanzaExcepcionCuandoNombreEsNull() {
            assertThatThrownBy(() -> playerService.findByNameContaining(null))
                    .isInstanceOf(InvalidInputException.class);
            verifyNoInteractions(playerRepository);
        }
    }

    // ── TeamServiceImpl.findByNameContaining ─────────────────────────────

    @Nested
    class TeamFindByName {

        @Mock private com.escuela.techcup.persistence.repository.tournament.TeamRepository teamRepository;
        @Mock private com.escuela.techcup.persistence.repository.tournament.TeamPlayerRepository teamPlayerRepository;
        @Mock private PlayerRepository playerRepository;
        @Mock private com.escuela.techcup.persistence.repository.tournament.InvitationRepository invitationRepository;
        @Mock private com.escuela.techcup.persistence.repository.tournament.TournamentRepository tournamentRepository;
        @Mock private com.escuela.techcup.persistence.repository.users.UserRepository userRepository;
        @Mock private com.escuela.techcup.persistence.repository.tournament.MatchRepository matchRepository;
        @Mock private com.escuela.techcup.persistence.repository.payment.PaymentRepository paymentRepository;
        @Mock private com.escuela.techcup.core.service.PaymentService paymentService;
        @InjectMocks private TeamServiceImpl teamService;

        private TeamEntity buildTeamEntity(String id, String name) {
            TeamEntity entity = new TeamEntity();
            entity.setId(id);
            entity.setName(name);
            entity.setUniformColor("rojo");
            entity.setFormation(Formation.FORMATION_4_4_2);
            return entity;
        }

        @Test
        void retornaEquipoCuandoExiste() {
            TeamEntity entity = buildTeamEntity("team-1", "Tigres FC");
            when(teamRepository.findByNameContainingIgnoreCase("Tigres"))
                    .thenReturn(List.of(entity));

            Optional<Team> result = teamService.findByNameContaining("Tigres");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Tigres FC");
            assertThat(result.get().getId()).isEqualTo("team-1");
        }

        @Test
        void retornaEmptyWhenNotFound() {
            when(teamRepository.findByNameContainingIgnoreCase("xyz"))
                    .thenReturn(List.of());

            Optional<Team> result = teamService.findByNameContaining("xyz");

            assertThat(result).isEmpty();
        }

        @Test
        void retornaPrimerResultadoCuandoHayVariosMatches() {
            TeamEntity t1 = buildTeamEntity("team-1", "Tigres A");
            TeamEntity t2 = buildTeamEntity("team-2", "Tigres B");
            when(teamRepository.findByNameContainingIgnoreCase("Tigres"))
                    .thenReturn(List.of(t1, t2));

            Optional<Team> result = teamService.findByNameContaining("Tigres");

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Tigres A");
        }

        @Test
        void lanzaExcepcionCuandoNombreEsBlank() {
            assertThatThrownBy(() -> teamService.findByNameContaining("  "))
                    .isInstanceOf(InvalidInputException.class);
            verifyNoInteractions(teamRepository);
        }

        @Test
        void lanzaExcepcionCuandoNombreEsNull() {
            assertThatThrownBy(() -> teamService.findByNameContaining(null))
                    .isInstanceOf(InvalidInputException.class);
            verifyNoInteractions(teamRepository);
        }
    }
}
