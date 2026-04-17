package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.TeamFullInfoDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.persistence.entity.tournament.*;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.tournament.*;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.core.model.enums.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamFullInfoServiceImplTest {

    @Mock private TeamRepository       teamRepository;
    @Mock private TeamPlayerRepository teamPlayerRepository;
    @Mock private MatchRepository      matchRepository;
    @Mock private GoalRepository       goalRepository;
    @Mock private TournamentRepository tournamentRepository;

    @InjectMocks private TeamFullInfoServiceImpl service;

    private TeamEntity       teamEntity;
    private TeamEntity       opponentEntity;
    private TournamentEntity tournamentEntity;
    private PlayerEntity     captainEntity;

    @BeforeEach
    void setUp() {
        UserPlayerEntity userEntity = new UserPlayerEntity();
        userEntity.setId("user-1");
        userEntity.setName("Capitán");
        userEntity.setMail("capitan@test.com");

        captainEntity = new PlayerEntity();
        captainEntity.setId("player-cap");
        captainEntity.setUser(userEntity);
        captainEntity.setPosition(Position.FORWARD);
        captainEntity.setDorsalNumber(10);

        tournamentEntity = new TournamentEntity();
        tournamentEntity.setId("tour-1");
        tournamentEntity.setStatus(TournamentStatus.ACTIVE);

        teamEntity = new TeamEntity();
        teamEntity.setId("team-1");
        teamEntity.setName("Los Tigres");
        teamEntity.setUniformColor("Rojo");
        teamEntity.setTournament(tournamentEntity);
        teamEntity.setCaptainPlayer(captainEntity);

        opponentEntity = new TeamEntity();
        opponentEntity.setId("team-2");
        opponentEntity.setName("Los Leones");
    }

    // ── getTeamFullInfo ──────────────────────────────────────────────────

    @Nested
    class GetTeamFullInfo {

        @Test
        void throwsWhenIdIsNull() {
            assertThatThrownBy(() -> service.getTeamFullInfo(null))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenIdIsBlank() {
            assertThatThrownBy(() -> service.getTeamFullInfo("  "))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenTeamNotFound() {
            when(teamRepository.findById("no-existe")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> service.getTeamFullInfo("no-existe"))
                    .isInstanceOf(TeamNotFoundException.class);
        }

        @Test
        void returnsBasicFields() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getTeamId()).isEqualTo("team-1");
            assertThat(result.getName()).isEqualTo("Los Tigres");
            assertThat(result.getUniformColor()).isEqualTo("Rojo");
            assertThat(result.getTournamentId()).isEqualTo("tour-1");
            assertThat(result.getCaptainName()).isEqualTo("Capitán");
        }

        @Test
        void tournamentNameTruncatedSafely() {
            // tourId = "tour-1" tiene 6 chars, Math.min(8,6) = 6 → "Torneo tour-1"
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getTournamentName()).isEqualTo("Torneo tour-1");
        }

        @Test
        void nullTournamentAndCaptainHandledGracefully() {
            teamEntity.setTournament(null);
            teamEntity.setCaptainPlayer(null);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getTournamentId()).isNull();
            assertThat(result.getTournamentName()).isNull();
            assertThat(result.getCaptainName()).isNull();
        }

        @Test
        void captainPlayerFlaggedCorrectly() {
            TeamPlayerEntity tp = new TeamPlayerEntity();
            tp.setPlayer(captainEntity);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(tp));
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getPlayers()).hasSize(1);
            assertThat(result.getPlayers().get(0).getPlayerId()).isEqualTo("player-cap");
            assertThat(result.getPlayers().get(0).isCaptain()).isTrue();
        }

        @Test
        void nonCaptainPlayerFlaggedFalse() {
            UserPlayerEntity other = new UserPlayerEntity();
            other.setId("user-2");
            other.setName("Jugador");
            other.setMail("jugador@test.com");

            PlayerEntity otherPlayer = new PlayerEntity();
            otherPlayer.setId("player-2");
            otherPlayer.setUser(other);
            otherPlayer.setPosition(Position.DEFENDER);
            otherPlayer.setDorsalNumber(5);

            TeamPlayerEntity tp = new TeamPlayerEntity();
            tp.setPlayer(otherPlayer);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of(tp));
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getPlayers().get(0).isCaptain()).isFalse();
        }

        @Test
        void emptyPlayersAndMatchesReturnsZeroStats() {
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getStats().getMatchesPlayed()).isZero();
            assertThat(result.getStats().getWins()).isZero();
            assertThat(result.getStats().getLosses()).isZero();
            assertThat(result.getStats().getDraws()).isZero();
            assertThat(result.getStats().getGoalsFor()).isZero();
            assertThat(result.getStats().getGoalsAgainst()).isZero();
        }
    }

    // ── Resultados de partidos ───────────────────────────────────────────

    @Nested
    class MatchResults {

        @Test
        void futureMatchIsPending() {
            MatchEntity future = buildMatch("m-1", teamEntity, opponentEntity,
                    LocalDateTime.now().plusDays(5));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(future));
            when(goalRepository.findByMatchId("m-1")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getResult()).isEqualTo("PENDING");
            assertThat(result.getStats().getMatchesPlayed()).isZero();
        }

        @Test
        void pastMatchWithMoreGoalsIsWin() {
            MatchEntity past = buildMatch("m-2", teamEntity, opponentEntity,
                    LocalDateTime.now().minusDays(1));

            captainEntity.setTeam(teamEntity);
            GoalEntity goal = new GoalEntity();
            goal.setPlayer(captainEntity);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(past));
            when(goalRepository.findByMatchId("m-2")).thenReturn(List.of(goal));

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getResult()).isEqualTo("WIN");
            assertThat(result.getStats().getWins()).isEqualTo(1);
            assertThat(result.getStats().getMatchesPlayed()).isEqualTo(1);
        }

        @Test
        void pastMatchWithFewerGoalsIsLoss() {
            MatchEntity past = buildMatch("m-3", teamEntity, opponentEntity,
                    LocalDateTime.now().minusDays(1));

            captainEntity.setTeam(opponentEntity);
            GoalEntity goal = new GoalEntity();
            goal.setPlayer(captainEntity);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(past));
            when(goalRepository.findByMatchId("m-3")).thenReturn(List.of(goal));

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getResult()).isEqualTo("LOSS");
            assertThat(result.getStats().getLosses()).isEqualTo(1);
        }

        @Test
        void pastMatchWithEqualGoalsIsDraw() {
            MatchEntity past = buildMatch("m-4", teamEntity, opponentEntity,
                    LocalDateTime.now().minusDays(1));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(past));
            when(goalRepository.findByMatchId("m-4")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getResult()).isEqualTo("DRAW");
            assertThat(result.getStats().getDraws()).isEqualTo(1);
        }

        @Test
        void goalWithNullPlayerTeamCountsAsAgainst() {
            MatchEntity past = buildMatch("m-5", teamEntity, opponentEntity,
                    LocalDateTime.now().minusDays(1));

            captainEntity.setTeam(null);
            GoalEntity goal = new GoalEntity();
            goal.setPlayer(captainEntity);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(past));
            when(goalRepository.findByMatchId("m-5")).thenReturn(List.of(goal));

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getGoalsAgainst()).isEqualTo(1);
            assertThat(result.getMatches().get(0).getGoalsFor()).isZero();
        }

        @Test
        void opponentNameCorrectWhenTeamIsTeamB() {
            // equipo es teamB
            MatchEntity past = buildMatch("m-6", opponentEntity, teamEntity,
                    LocalDateTime.now().minusDays(1));

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of(past));
            when(goalRepository.findByMatchId("m-6")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getMatches().get(0).getOpponentName()).isEqualTo("Los Leones");
        }

        @Test
        void multipleMatchesAccumulateStats() {
            MatchEntity win  = buildMatch("m-w", teamEntity, opponentEntity, LocalDateTime.now().minusDays(3));
            MatchEntity loss = buildMatch("m-l", teamEntity, opponentEntity, LocalDateTime.now().minusDays(2));
            MatchEntity pend = buildMatch("m-p", teamEntity, opponentEntity, LocalDateTime.now().plusDays(1));

            // Jugador del equipo local → gol a favor en el partido ganado
            UserPlayerEntity u1 = new UserPlayerEntity();
            u1.setId("u-local");
            PlayerEntity localPlayer = new PlayerEntity();
            localPlayer.setId("p-local");
            localPlayer.setUser(u1);
            localPlayer.setTeam(teamEntity);

            // Jugador del equipo rival → gol en contra en el partido perdido
            UserPlayerEntity u2 = new UserPlayerEntity();
            u2.setId("u-rival");
            PlayerEntity rivalPlayer = new PlayerEntity();
            rivalPlayer.setId("p-rival");
            rivalPlayer.setUser(u2);
            rivalPlayer.setTeam(opponentEntity);

            GoalEntity g1 = new GoalEntity(); g1.setPlayer(localPlayer);
            GoalEntity g2 = new GoalEntity(); g2.setPlayer(rivalPlayer);

            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1"))
                    .thenReturn(List.of(win, loss, pend));
            when(goalRepository.findByMatchId("m-w")).thenReturn(List.of(g1));
            when(goalRepository.findByMatchId("m-l")).thenReturn(List.of(g2));
            when(goalRepository.findByMatchId("m-p")).thenReturn(List.of());

            TeamFullInfoDTO result = service.getTeamFullInfo("team-1");

            assertThat(result.getStats().getMatchesPlayed()).isEqualTo(2);
            assertThat(result.getStats().getWins()).isEqualTo(1);
            assertThat(result.getStats().getLosses()).isEqualTo(1);
            assertThat(result.getMatches()).hasSize(3);
        }
    }

    // ── getTeamsByTournament ─────────────────────────────────────────────

    @Nested
    class GetTeamsByTournament {

        @Test
        void throwsWhenTournamentIdIsNull() {
            assertThatThrownBy(() -> service.getTeamsByTournament(null))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenTournamentIdIsBlank() {
            assertThatThrownBy(() -> service.getTeamsByTournament("  "))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void throwsWhenTournamentNotFound() {
            when(tournamentRepository.findById("no-existe")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> service.getTeamsByTournament("no-existe"))
                    .isInstanceOf(InvalidInputException.class);
        }

        @Test
        void returnsOnlyTeamsOfTournament() {
            when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
            when(teamRepository.findAll()).thenReturn(List.of(teamEntity));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            var result = service.getTeamsByTournament("tour-1");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTeamId()).isEqualTo("team-1");
        }

        @Test
        void returnsEmptyWhenNoTeams() {
            when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
            when(teamRepository.findAll()).thenReturn(List.of());

            assertThat(service.getTeamsByTournament("tour-1")).isEmpty();
        }

        @Test
        void filtersTeamsFromOtherTournaments() {
            TournamentEntity other = new TournamentEntity();
            other.setId("tour-2");

            TeamEntity otherTeam = new TeamEntity();
            otherTeam.setId("team-other");
            otherTeam.setName("Otro");
            otherTeam.setTournament(other);

            when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
            when(teamRepository.findAll()).thenReturn(List.of(teamEntity, otherTeam));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            var result = service.getTeamsByTournament("tour-1");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTeamId()).isEqualTo("team-1");
        }

        @Test
        void filtersTeamsWithNullTournament() {
            TeamEntity noTour = new TeamEntity();
            noTour.setId("team-no-tour");
            noTour.setName("Sin torneo");
            noTour.setTournament(null);

            when(tournamentRepository.findById("tour-1")).thenReturn(Optional.of(tournamentEntity));
            when(teamRepository.findAll()).thenReturn(List.of(teamEntity, noTour));
            when(teamPlayerRepository.findByTeamId("team-1")).thenReturn(List.of());
            when(matchRepository.findByTeamAIdOrTeamBId("team-1", "team-1")).thenReturn(List.of());

            assertThat(service.getTeamsByTournament("tour-1")).hasSize(1);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────

    private MatchEntity buildMatch(String id, TeamEntity teamA, TeamEntity teamB,
                                   LocalDateTime dateTime) {
        MatchEntity match = new MatchEntity();
        match.setId(id);
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setDateTime(dateTime);
        return match;
    }
}