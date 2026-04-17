package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.PaymentDTO;
import com.escuela.techcup.core.exception.*;
import com.escuela.techcup.core.model.Invitation;
import com.escuela.techcup.core.model.Payment;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.*;
import com.escuela.techcup.core.service.PaymentService;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.entity.tournament.*;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.repository.tournament.*;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock private TeamRepository       teamRepository;
    @Mock private TeamPlayerRepository teamPlayerRepository;
    @Mock private PlayerRepository     playerRepository;
    @Mock private InvitationRepository invitationRepository;
    @Mock private TournamentRepository tournamentRepository;
    @Mock private UserRepository       userRepository;
    @Mock private MatchRepository      matchRepository;

    @InjectMocks private TeamServiceImpl teamService;




    @Mock private PaymentService paymentService;
    private TeamEntity mockTeamEntity;
    private TournamentEntity mockTournamentEntity;
    private Payment mockPayment;

    private TeamEntity teamEntity;
    private PlayerEntity playerEntity;
    private UserPlayerEntity userEntity;
    private TournamentEntity tournamentEntity;

    @BeforeEach
    void setUp() {
        userEntity = new UserPlayerEntity();
        userEntity.setId("user-1");

        playerEntity = new PlayerEntity();
        playerEntity.setId("player-1");
        playerEntity.setUser(userEntity);

        tournamentEntity = new TournamentEntity();
        tournamentEntity.setId("tour-1");
        tournamentEntity.setStatus(TournamentStatus.ACTIVE);

        teamEntity = new TeamEntity();
        teamEntity.setId("team-1");
        teamEntity.setName("Los Tigres");
        teamEntity.setUniformColor("Rojo y Negro");
        teamEntity.setTournament(tournamentEntity);

        mockTournamentEntity = mock(TournamentEntity.class);
        mockTeamEntity = mock(TeamEntity.class);

        mockPayment = new Payment();
        mockPayment.setId("pay-1");
        mockPayment.setStatus(PaymentStatus.PENDING);
    }

    // ── createTeam ───────────────────────────────────────────────────────

    @Nested
    class CreateTeam {

        @Test
        void createsTeamSuccessfully() {
            when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
            when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of(tournamentEntity));
            when(playerRepository.findByUserId("user-1")).thenReturn(Optional.of(playerEntity));
            when(userRepository.save(any())).thenReturn(userEntity);
            when(teamRepository.save(any())).thenReturn(teamEntity);

            Team result = teamService.createTeam("Los Tigres", "Rojo y Negro", null, "user-1");

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Los Tigres");
            verify(teamPlayerRepository).save(any());
            verify(userRepository).save(any());
        }

        @Test
        void throwsWhenNameIsNull() {
            assertThatThrownBy(() -> teamService.createTeam(null, "red", null, "cap-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Team name is required");
        }

        @Test
        void throwsWhenNameIsBlank() {
            assertThatThrownBy(() -> teamService.createTeam("   ", "red", null, "cap-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Team name is required");
        }

        @Test
        void throwsWhenNameTooShort() {
            assertThatThrownBy(() -> teamService.createTeam("AB", "red", null, "cap-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("at least 5 characters");
        }

        @Test
        void throwsWhenUniformColorsIsNull() {
            assertThatThrownBy(() -> teamService.createTeam("Dream Team", null, null, "cap-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Uniform colors are required");
        }

        @Test
        void throwsWhenUniformColorsIsBlank() {
            assertThatThrownBy(() -> teamService.createTeam("Dream Team", "  ", null, "cap-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Uniform colors are required");
        }

        @Test
        void throwsWhenCaptainIdIsNull() {
            assertThatThrownBy(() -> teamService.createTeam("Los Tigres", "Rojo", null, null))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenTeamNameAlreadyExists() {
            when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(true);

            assertThatThrownBy(() -> teamService.createTeam("Los Tigres", "red", null, "cap-1"))
                    .isInstanceOf(TeamAlreadyExistsException.class);
        }

        @Test
        void throwsWhenNoActiveTournament() {
            when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
            when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of());

            assertThatThrownBy(() -> teamService.createTeam("Los Tigres", "Rojo", null, "user-1"))
                    .isInstanceOf(TournamentNotActiveException.class);
        }

        @Test
        void throwsWhenCaptainPlayerNotFound() {
            when(teamRepository.existsByNameIgnoreCase("Los Tigres")).thenReturn(false);
            when(tournamentRepository.findByStatus(TournamentStatus.ACTIVE)).thenReturn(List.of(tournamentEntity));
            when(playerRepository.findByUserId("user-1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.createTeam("Los Tigres", "Rojo", null, "user-1"))
                    .isInstanceOf(InvalidInputException.class);
        }
    }

    // ── invitePlayer ─────────────────────────────────────────────────────

    @Nested
    class InvitePlayer {

        @Test
        void invitesPlayerSuccessfully() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());

            teamService.invitePlayer("team-1", "player-1", "Join us!");

            verify(invitationRepository).save(any(InvitationEntity.class));
        }

        @Test
        void throwsWhenTeamIdIsNull() {
            assertThatThrownBy(() -> teamService.invitePlayer(null, "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }

        @Test
        void throwsWhenTeamIdIsBlank() {
            assertThatThrownBy(() -> teamService.invitePlayer("  ", "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }

        @Test
        void throwsWhenPlayerIdIsNull() {
            assertThatThrownBy(() -> teamService.invitePlayer("team-1", null, "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("playerId is required");
        }

        @Test
        void throwsWhenPlayerIdIsBlank() {
            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "  ", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("playerId is required");
        }

        @Test
        void throwsWhenTeamNotFound() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(TeamNotFoundException.class);
        }

        @Test
        void throwsWhenPlayerNotFound() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(playerRepository.findById("player-1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Player not found");
        }

        @Test
        void throwsWhenPlayerAlreadyInvited() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(true);

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(PlayerAlreadyInvitedException.class);
        }

        @Test
        void throwsWhenTeamFull() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(12));

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("maximum of 12");
        }

        @Test
        void throwsWhenPlayerAlreadyInTournament() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(teamPlayerRepository.existsByPlayerIdAndTournamentId("player-1", "tour-1")).thenReturn(true);

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class);
        }
    }

    // ── respondInvitation ────────────────────────────────────────────────

    @Nested
    class RespondInvitation {

        @Test
        void acceptsInvitationAndCreatesTeamPlayer() {
            InvitationEntity invitation = buildPendingInvitation();
            when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                    .thenReturn(Optional.of(invitation));

            teamService.respondInvitation("inv-1", InvitationStatus.ACCEPTED);

            verify(teamPlayerRepository).save(any(TeamPlayerEntity.class));
            verify(userRepository).save(any());
            assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
        }

        @Test
        void rejectsInvitationWithoutCreatingTeamPlayer() {
            InvitationEntity invitation = buildPendingInvitation();
            when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                    .thenReturn(Optional.of(invitation));

            teamService.respondInvitation("inv-1", InvitationStatus.REJECTED);

            verify(teamPlayerRepository, never()).save(any());
            verify(userRepository, never()).save(any());
            assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.REJECTED);
        }

        @Test
        void throwsWhenInvitationIdIsNull() {
            assertThatThrownBy(() -> teamService.respondInvitation(null, InvitationStatus.ACCEPTED))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("invitationId is required");
        }

        @Test
        void throwsWhenInvitationIdIsBlank() {
            assertThatThrownBy(() -> teamService.respondInvitation("  ", InvitationStatus.ACCEPTED))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("invitationId is required");
        }

        @Test
        void throwsWhenActionIsNull() {
            assertThatThrownBy(() -> teamService.respondInvitation("inv-1", null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("action is required");
        }

        @Test
        void throwsWhenInvitationNotFound() {
            when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.respondInvitation("inv-1", InvitationStatus.ACCEPTED))
                    .isInstanceOf(InvitationNotFoundException.class);
        }

        private InvitationEntity buildPendingInvitation() {
            InvitationEntity inv = new InvitationEntity();
            inv.setId("inv-1");
            inv.setTeam(teamEntity);
            inv.setPlayer(playerEntity);
            inv.setStatus(InvitationStatus.PENDING);
            return inv;
        }
    }

    // ── getInvitationsByPlayer ────────────────────────────────────────────

    @Nested
    class GetInvitationsByPlayer {

        @Test
        void returnsListWhenFound() {
            InvitationEntity inv = new InvitationEntity();
            inv.setId("inv-1");
            inv.setTeam(teamEntity);
            inv.setPlayer(playerEntity);
            inv.setStatus(InvitationStatus.PENDING);

            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.findByPlayerId("player-1")).thenReturn(List.of(inv));

            List<Invitation> result = teamService.getInvitationsByPlayer("player-1");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getId()).isEqualTo("inv-1");
            assertThat(result.get(0).getTeamId()).isEqualTo("team-1");
        }

        @Test
        void returnsEmptyWhenNone() {
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(playerEntity));
            when(invitationRepository.findByPlayerId("player-1")).thenReturn(List.of());

            assertThat(teamService.getInvitationsByPlayer("player-1")).isEmpty();
        }

        @Test
        void throwsWhenPlayerIdIsNull() {
            assertThatThrownBy(() -> teamService.getInvitationsByPlayer(null))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenPlayerNotFound() {
            when(playerRepository.findById("no-existe")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.getInvitationsByPlayer("no-existe"))
                    .isInstanceOf(InvalidInputException.class);
        }
    }

    // ── validateTeamComposition ──────────────────────────────────────────

    @Nested
    class ValidateTeamComposition {

        @Test
        void returnsTrueAtLowerBoundary() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(7));
            assertThat(teamService.validateTeamComposition("team-1")).isTrue();
        }

        @Test
        void returnsTrueAtUpperBoundary() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(12));
            assertThat(teamService.validateTeamComposition("team-1")).isTrue();
        }

        @Test
        void returnsTrueWithinRange() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(9));
            assertThat(teamService.validateTeamComposition("team-1")).isTrue();
        }

        @Test
        void returnsFalseWhenFewerThanSevenPlayers() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(6));
            assertThat(teamService.validateTeamComposition("team-1")).isFalse();
        }

        @Test
        void returnsFalseWhenMoreThanTwelvePlayers() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(buildPlayerList(13));
            assertThat(teamService.validateTeamComposition("team-1")).isFalse();
        }

        @Test
        void throwsWhenTeamIdIsNull() {
            assertThatThrownBy(() -> teamService.validateTeamComposition(null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }

        @Test
        void throwsWhenTeamIdIsBlank() {
            assertThatThrownBy(() -> teamService.validateTeamComposition("  "))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }
    }

    // ── validatePlayerUniquePerTournament ────────────────────────────────

    @Nested
    class ValidatePlayerUniquePerTournament {

        @Test
        void returnsTrueWhenPlayerNotInTournament() {
            when(teamPlayerRepository.existsByPlayerIdAndTournamentId("p1", "t1")).thenReturn(false);
            assertThat(teamService.validatePlayerUniquePerTournament("p1", "t1")).isTrue();
        }

        @Test
        void returnsFalseWhenPlayerAlreadyInTournament() {
            when(teamPlayerRepository.existsByPlayerIdAndTournamentId("p1", "t1")).thenReturn(true);
            assertThat(teamService.validatePlayerUniquePerTournament("p1", "t1")).isFalse();
        }

        @Test
        void throwsWhenPlayerIdIsNull() {
            assertThatThrownBy(() -> teamService.validatePlayerUniquePerTournament(null, "t1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("playerId is required");
        }

        @Test
        void throwsWhenPlayerIdIsBlank() {
            assertThatThrownBy(() -> teamService.validatePlayerUniquePerTournament("  ", "t1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("playerId is required");
        }

        @Test
        void throwsWhenTournamentIdIsNull() {
            assertThatThrownBy(() -> teamService.validatePlayerUniquePerTournament("p1", null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("tournamentId is required");
        }

        @Test
        void throwsWhenTournamentIdIsBlank() {
            assertThatThrownBy(() -> teamService.validatePlayerUniquePerTournament("p1", "  "))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("tournamentId is required");
        }
    }

    // ── getTeamById ──────────────────────────────────────────────────────

    @Nested
    class GetTeamById {

        @Test
        void returnsTeamWhenFound() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            Team result = teamService.getTeamById("team-1");
            assertThat(result).isNotNull();
        }

        @Test
        void throwsWhenTeamNotFound() {
            when(teamRepository.findById("team-x")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> teamService.getTeamById("team-x"))
                    .isInstanceOf(TeamNotFoundException.class);
        }

        @Test
        void throwsWhenTeamIdIsNull() {
            assertThatThrownBy(() -> teamService.getTeamById(null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }

        @Test
        void throwsWhenTeamIdIsBlank() {
            assertThatThrownBy(() -> teamService.getTeamById("  "))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }
    }

    // ── getAllTeams ──────────────────────────────────────────────────────

    @Nested
    class GetAllTeams {

        @Test
        void returnsEmptyListWhenNoTeams() {
            when(teamRepository.findAll()).thenReturn(List.of());
            assertThat(teamService.getAllTeams()).isEmpty();
        }

        @Test
        void returnsMappedTeams() {
            when(teamRepository.findAll()).thenReturn(List.of(teamEntity));
            assertThat(teamService.getAllTeams()).hasSize(1);
        }
    }

    // ── validateEngineeringMajority ──────────────────────────────────────

    @Nested
    class ValidateEngineeringMajority {

        @Test
        void returnsTrueWhenMajorityAreStudents() {
            StudentEntity s = new StudentEntity(); s.setCareer(Career.ENGINEERING);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(s), buildTeamPlayer(s), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWhenMajorityAreTeachers() {
            TeacherEntity t = new TeacherEntity(); t.setCareer(Career.DATA_SCIENCE);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(t), buildTeamPlayer(t), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWhenMajorityAreGraduates() {
            GraduateEntity g = new GraduateEntity(); g.setCareer(Career.ENGINEERING);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(g), buildTeamPlayer(g), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWithMixedEngineeringTypes() {
            StudentEntity s = new StudentEntity(); s.setCareer(Career.ENGINEERING);
            TeacherEntity t = new TeacherEntity(); t.setCareer(Career.DATA_SCIENCE);
            GraduateEntity g = new GraduateEntity(); g.setCareer(Career.ENGINEERING);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(s), buildTeamPlayer(t), buildTeamPlayer(g), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsFalseWhenMajorityAreFamiliars() {
            StudentEntity s = new StudentEntity(); s.setCareer(Career.OTHER);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(s), buildTeamPlayer(new FamiliarEntity()), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isFalse();
        }

        @Test
        void returnsFalseWhenExactlyHalfAreEngineering() {
            StudentEntity s = new StudentEntity(); s.setCareer(Career.ENGINEERING);
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(
                    buildTeamPlayer(s), buildTeamPlayer(new FamiliarEntity())
            ));
            assertThat(teamService.validateEngineeringMajority("team-1")).isFalse();
        }

        @Test
        void throwsWhenTeamHasNoPlayers() {
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            assertThatThrownBy(() -> teamService.validateEngineeringMajority("team-1"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Team has no players");
        }

        @Test
        void throwsWhenTeamIdIsNull() {
            assertThatThrownBy(() -> teamService.validateEngineeringMajority(null))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }

        @Test
        void throwsWhenTeamIdIsBlank() {
            assertThatThrownBy(() -> teamService.validateEngineeringMajority("  "))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("teamId is required");
        }
    }

    // ── changeFormation ──────────────────────────────────────────────────

    @Nested
    class ChangeFormation {

        @Test
        void changesFormationSuccessfully() {
            MatchEntity match = new MatchEntity();
            match.setDateTime(LocalDateTime.now().plusHours(3));
            when(matchRepository.findByIdAndTeam("match-1", "team-1")).thenReturn(Optional.of(match));
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));

            teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-1");

            assertThat(teamEntity.getFormation()).isEqualTo(Formation.FORMATION_4_3_3);
            verify(teamRepository).save(teamEntity);
        }

        @Test
        void throwsWhenMatchNotFound() {
            when(matchRepository.findByIdAndTeam("match-x", "team-1")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-x"))
                    .isInstanceOf(MatchNotFoundException.class);
        }

        @Test
        void throwsWhenMatchHasAlreadyStarted() {
            MatchEntity match = new MatchEntity();
            match.setDateTime(LocalDateTime.now().minusMinutes(1));
            when(matchRepository.findByIdAndTeam("match-1", "team-1")).thenReturn(Optional.of(match));
            assertThatThrownBy(() -> teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-1"))
                    .isInstanceOf(ScheduleConflictException.class)
                    .hasMessageContaining("already started");
        }

        @Test
        void throwsWhenMatchStartsWithinOneHour() {
            MatchEntity match = new MatchEntity();
            match.setDateTime(LocalDateTime.now().plusMinutes(30));
            when(matchRepository.findByIdAndTeam("match-1", "team-1")).thenReturn(Optional.of(match));
            assertThatThrownBy(() -> teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-1"))
                    .isInstanceOf(ScheduleConflictException.class)
                    .hasMessageContaining("minutes");
        }

        @Test
        void throwsWhenTeamNotFound() {
            MatchEntity match = new MatchEntity();
            match.setDateTime(LocalDateTime.now().plusHours(3));
            when(matchRepository.findByIdAndTeam("match-1", "team-1")).thenReturn(Optional.of(match));
            when(teamRepository.findById("team-1")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-1"))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    // ── getAllFormations ─────────────────────────────────────────────────

    @Nested
    class GetAllFormations {

        @Test
        void returnsAllEnumValues() {
            List<Formation> result = teamService.getAllFormations();
            assertThat(result).containsExactlyInAnyOrder(Formation.values());
        }
    }

    // ── getEnemyFormation ────────────────────────────────────────────────

    @Nested
    class GetEnemyFormation {

        @Test
        void returnsFormationOfTeam() {
            teamEntity.setFormation(Formation.FORMATION_4_4_2);
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            assertThat(teamService.getEnemyFormation("team-1")).isEqualTo(Formation.FORMATION_4_4_2);
        }

        @Test
        void throwsWhenTeamNotFound() {
            when(teamRepository.findById("team-x")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> teamService.getEnemyFormation("team-x"))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }

    // ── UploadPayment ────────────────────────────────────────────────────

    @Nested
    class UploadPayment {

        private PaymentDTO buildDTO(LocalDateTime paymentDate) {
            PaymentDTO dto = new PaymentDTO();
            dto.setDescription("Comprobante torneo");
            dto.setPaymentDate(paymentDate);
            dto.setComprobante(mockMultipartFile());
            return dto;
        }

        private MultipartFile mockMultipartFile() {
            // TeamService solo pasa el archivo al PaymentService (mockeado),
            // no lo lee directamente, así que no necesita stubs
            return mock(MultipartFile.class);
        }

        @Test
        void whenValidPayment_thenSavesTeamAndReturnsPayment() {
            PaymentDTO dto = buildDTO(LocalDateTime.of(2025, 3, 1, 10, 0));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(mockTeamEntity));
            when(mockTeamEntity.getTournament()).thenReturn(mockTournamentEntity);
            when(mockTournamentEntity.getStartDate()).thenReturn(LocalDateTime.of(2025, 4, 1, 0, 0));
            when(paymentService.createPayment(any(), any())).thenReturn(mockPayment);

            Payment result = teamService.uploadPayment("team-1", dto);

            assertNotNull(result);
            assertEquals("pay-1", result.getId());
            assertEquals(PaymentStatus.PENDING, result.getStatus());
            verify(teamRepository).save(mockTeamEntity);
        }

        @Test
        void whenTeamNotFound_thenThrowsEntityNotFoundException() {
            PaymentDTO dto = buildDTO(LocalDateTime.of(2025, 3, 1, 10, 0));
            when(teamRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> teamService.uploadPayment("unknown", dto));
            verifyNoInteractions(paymentService);
        }

        @Test
        void whenPaymentDateAfterDeadline_thenThrowsPaymentDateException() {
            PaymentDTO dto = buildDTO(LocalDateTime.of(2025, 5, 1, 10, 0));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(mockTeamEntity));
            when(mockTeamEntity.getTournament()).thenReturn(mockTournamentEntity);
            when(mockTournamentEntity.getStartDate()).thenReturn(LocalDateTime.of(2025, 4, 1, 0, 0));

            assertThrows(PaymentDateException.class,
                    () -> teamService.uploadPayment("team-1", dto));
            verifyNoInteractions(paymentService);
        }

        @Test
        void whenPaymentDateEqualsDeadline_thenThrowsPaymentDateException() {
            LocalDateTime deadline = LocalDateTime.of(2025, 4, 1, 0, 0);
            PaymentDTO dto = buildDTO(deadline);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(mockTeamEntity));
            when(mockTeamEntity.getTournament()).thenReturn(mockTournamentEntity);
            when(mockTournamentEntity.getStartDate()).thenReturn(deadline);

            assertThrows(PaymentDateException.class,
                    () -> teamService.uploadPayment("team-1", dto));
            verifyNoInteractions(paymentService);
        }

        @Test
        void whenPaymentDateOneSecondBeforeDeadline_thenSucceeds() {
            LocalDateTime deadline = LocalDateTime.of(2025, 4, 1, 0, 0);
            PaymentDTO dto = buildDTO(deadline.minusSeconds(1));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(mockTeamEntity));
            when(mockTeamEntity.getTournament()).thenReturn(mockTournamentEntity);
            when(mockTournamentEntity.getStartDate()).thenReturn(deadline);
            when(paymentService.createPayment(any(), any())).thenReturn(mockPayment);

            Payment result = teamService.uploadPayment("team-1", dto);

            assertNotNull(result);
            verify(teamRepository).save(mockTeamEntity);
        }

        @Test
        void whenValidPayment_thenAssociatesPaymentToTeam() {
            PaymentDTO dto = buildDTO(LocalDateTime.of(2025, 3, 1, 10, 0));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(mockTeamEntity));
            when(mockTeamEntity.getTournament()).thenReturn(mockTournamentEntity);
            when(mockTournamentEntity.getStartDate()).thenReturn(LocalDateTime.of(2025, 4, 1, 0, 0));
            when(paymentService.createPayment(any(), any())).thenReturn(mockPayment);

            teamService.uploadPayment("team-1", dto);

            verify(mockTeamEntity).setPayment(any(PaymentEntity.class));
        }
    }


    // ── Helpers ──────────────────────────────────────────────────────────

    private List<TeamPlayerEntity> buildPlayerList(int count) {
        List<TeamPlayerEntity> list = new ArrayList<>();
        for (int i = 0; i < count; i++) list.add(new TeamPlayerEntity());
        return list;
    }

    private TeamPlayerEntity buildTeamPlayer(UserPlayerEntity userEntity) {
        PlayerEntity player = new PlayerEntity();
        player.setUser(userEntity);
        TeamPlayerEntity tpe = new TeamPlayerEntity();
        tpe.setPlayer(player);
        return tpe;
    }
}