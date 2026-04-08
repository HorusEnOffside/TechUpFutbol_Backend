package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.exception.*;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.InvitationStatus;
import com.escuela.techcup.persistence.entity.tournament.*;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.repository.tournament.*;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock private TeamRepository       teamRepository;
    @Mock private TeamPlayerRepository teamPlayerRepository;
    @Mock private PlayerRepository     playerRepository;
    @Mock private InvitationRepository invitationRepository;
    @Mock private MatchRepository      matchRepository;

    @InjectMocks
    private TeamServiceImpl teamService;




    @Nested
    class CreateTeam {

        @Test
        void createsTeamSuccessfully() {
            when(teamRepository.existsByNameIgnoreCase("Dream Team")).thenReturn(false);

            Team result = teamService.createTeam("Dream Team", "red-white", null, "captain-1");

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Dream Team");
            verify(teamRepository).save(any(TeamEntity.class));
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
        void throwsWhenTeamNameAlreadyExists() {
            when(teamRepository.existsByNameIgnoreCase("Dream Team")).thenReturn(true);

            assertThatThrownBy(() -> teamService.createTeam("Dream Team", "red", null, "cap-1"))
                    .isInstanceOf(TeamAlreadyExistsException.class);
        }
    }



    @Nested
    class InvitePlayer {

        @Test
        void invitesPlayerSuccessfully() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(new TeamEntity()));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(new PlayerEntity()));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(false);

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
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(new TeamEntity()));
            when(playerRepository.findById("player-1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(InvalidInputException.class)
                    .hasMessageContaining("Player not found");
        }

        @Test
        void throwsWhenPlayerAlreadyInvited() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(new TeamEntity()));
            when(playerRepository.findById("player-1")).thenReturn(Optional.of(new PlayerEntity()));
            when(invitationRepository.existsByTeamIdAndPlayerId("team-1", "player-1")).thenReturn(true);

            assertThatThrownBy(() -> teamService.invitePlayer("team-1", "player-1", "msg"))
                    .isInstanceOf(PlayerAlreadyInvitedException.class);
        }
    }



    @Nested
    class RespondInvitation {

        @Test
        void acceptsInvitationAndCreatesTeamPlayer() {
            InvitationEntity invitation = buildPendingInvitation();
            when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                    .thenReturn(Optional.of(invitation));

            teamService.respondInvitation("inv-1", InvitationStatus.ACCEPTED);

            verify(teamPlayerRepository).save(any(TeamPlayerEntity.class));
            assertThat(invitation.getStatus()).isEqualTo(InvitationStatus.ACCEPTED);
        }

        @Test
        void rejectsInvitationWithoutCreatingTeamPlayer() {
            InvitationEntity invitation = buildPendingInvitation();
            when(invitationRepository.findByIdAndStatus("inv-1", InvitationStatus.PENDING))
                    .thenReturn(Optional.of(invitation));

            teamService.respondInvitation("inv-1", InvitationStatus.REJECTED);

            verify(teamPlayerRepository, never()).save(any());
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
            inv.setTeam(new TeamEntity());
            inv.setPlayer(new PlayerEntity());
            inv.setStatus(InvitationStatus.PENDING);
            return inv;
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
            TeamEntity entity = new TeamEntity();
            entity.setId("team-1");
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(entity));

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
            when(teamRepository.findAll()).thenReturn(List.of(new TeamEntity()));

            assertThat(teamService.getAllTeams()).hasSize(1);
        }
    }


    // ── validateEngineeringMajority ──────────────────────────────────────

    @Nested
    class ValidateEngineeringMajority {

        @Test
        void returnsTrueWhenMajorityAreStudents() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new StudentEntity()),
                    buildTeamPlayer(new StudentEntity()),
                    buildTeamPlayer(new FamiliarEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWhenMajorityAreTeachers() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new TeacherEntity()),
                    buildTeamPlayer(new TeacherEntity()),
                    buildTeamPlayer(new FamiliarEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWhenMajorityAreGraduates() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new GraduateEntity()),
                    buildTeamPlayer(new GraduateEntity()),
                    buildTeamPlayer(new FamiliarEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsTrueWithMixedEngineeringTypes() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new StudentEntity()),
                    buildTeamPlayer(new TeacherEntity()),
                    buildTeamPlayer(new GraduateEntity()),
                    buildTeamPlayer(new FamiliarEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

            assertThat(teamService.validateEngineeringMajority("team-1")).isTrue();
        }

        @Test
        void returnsFalseWhenMajorityAreFamiliars() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new FamiliarEntity()),
                    buildTeamPlayer(new FamiliarEntity()),
                    buildTeamPlayer(new StudentEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

            assertThat(teamService.validateEngineeringMajority("team-1")).isFalse();
        }

        @Test
        void returnsFalseWhenExactlyHalfAreEngineering() {
            List<TeamPlayerEntity> players = List.of(
                    buildTeamPlayer(new StudentEntity()),
                    buildTeamPlayer(new FamiliarEntity())
            );
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(players);

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
            TeamEntity team = new TeamEntity();
            when(matchRepository.findByIdAndTeam("match-1", "team-1")).thenReturn(Optional.of(match));
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(team));

            teamService.changeFormation(Formation.FORMATION_4_3_3, "team-1", "match-1");

            assertThat(team.getFormation()).isEqualTo(Formation.FORMATION_4_3_3);
            verify(teamRepository).save(team);
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
            TeamEntity team = new TeamEntity();
            team.setFormation(Formation.FORMATION_4_4_2);
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(team));

            Formation result = teamService.getEnemyFormation("team-1");

            assertThat(result).isEqualTo(Formation.FORMATION_4_4_2);
        }

        @Test
        void throwsWhenTeamNotFound() {
            when(teamRepository.findById("team-x")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> teamService.getEnemyFormation("team-x"))
                    .isInstanceOf(TeamNotFoundException.class);
        }
    }


    // ── Helpers ──────────────────────────────────────────────────────────

    private List<TeamPlayerEntity> buildPlayerList(int count) {
        List<TeamPlayerEntity> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TeamPlayerEntity());
        }
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
