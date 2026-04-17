package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.MatchResultDTO;
import com.escuela.techcup.controller.dto.PlayerMatchStatsDTO;
import com.escuela.techcup.core.exception.MatchNotFoundException;
import com.escuela.techcup.core.exception.TeamNotFoundException;
import com.escuela.techcup.core.exception.UserNotFoundException;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.SoccerFieldService;
import com.escuela.techcup.persistence.entity.tournament.CardEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.RefereeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private RefereeRepository refereeRepository;

    @Mock
    private SoccerFieldService soccerFieldService;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    private TeamEntity teamEntityA;
    private TeamEntity teamEntityB;
    private MatchEntity matchEntity;

    @BeforeEach
    void setUp() {
        teamEntityA = buildTeamEntity("team-a", "Team Alpha");
        teamEntityB = buildTeamEntity("team-b", "Team Beta");

        matchEntity = new MatchEntity();
        matchEntity.setId("match-1");
        matchEntity.setDateTime(LocalDateTime.now());
        matchEntity.setTeamA(teamEntityA);
        matchEntity.setTeamB(teamEntityB);
        matchEntity.setGoals(new ArrayList<>());
    }


    @Nested
    class GetMatchById {

        @Test
        void whenMatchExists_thenReturnsMatch() {
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));

            Match result = matchService.getMatchById("match-1");

            assertNotNull(result);
            assertEquals("match-1", result.getId());
        }

        @Test
        void whenMatchDoesNotExist_thenThrowsMatchNotFoundException() {
            when(matchRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(MatchNotFoundException.class,
                    () -> matchService.getMatchById("unknown"));
        }
    }



    @Nested
    class GetAllMatches {

        @Test
        void whenMatchesExist_thenReturnsAllMatches() {
            MatchEntity entity2 = new MatchEntity();
            entity2.setId("match-2");
            entity2.setDateTime(LocalDateTime.now());
            entity2.setTeamA(teamEntityA);
            entity2.setTeamB(teamEntityB);
            entity2.setGoals(new ArrayList<>());

            when(matchRepository.findAll()).thenReturn(List.of(matchEntity, entity2));

            List<Match> result = matchService.getAllMatches();

            assertEquals(2, result.size());
            verify(matchRepository).findAll();
        }

        @Test
        void whenNoMatchesExist_thenReturnsEmptyList() {
            when(matchRepository.findAll()).thenReturn(List.of());

            List<Match> result = matchService.getAllMatches();

            assertTrue(result.isEmpty());
        }
    }



    @Nested
    class CreateMatch {

        @Test
        void whenBothTeamsExist_thenCreatesMatchSuccessfully() {
            when(teamRepository.findById("team-a")).thenReturn(Optional.of(teamEntityA));
            when(teamRepository.findById("team-b")).thenReturn(Optional.of(teamEntityB));
            when(matchRepository.save(any(MatchEntity.class))).thenAnswer(i -> i.getArgument(0));

            Match result = matchService.createMatch(LocalDate.of(2025, 6, 15), "team-a", "team-b");

            assertNotNull(result);
            assertNotNull(result.getId());
            assertEquals("Team Alpha", result.getTeamA().getName());
            assertEquals("Team Beta", result.getTeamB().getName());
            verify(matchRepository).save(any(MatchEntity.class));
        }

        @Test
        void whenTeamADoesNotExist_thenThrowsTeamNotFoundException() {
            when(teamRepository.findById("team-a")).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class,
                    () -> matchService.createMatch(LocalDate.now(), "team-a", "team-b"));

            verify(matchRepository, never()).save(any());
        }

        @Test
        void whenTeamBDoesNotExist_thenThrowsTeamNotFoundException() {
            when(teamRepository.findById("team-a")).thenReturn(Optional.of(teamEntityA));
            when(teamRepository.findById("team-b")).thenReturn(Optional.empty());

            assertThrows(TeamNotFoundException.class,
                    () -> matchService.createMatch(LocalDate.now(), "team-a", "team-b"));

            verify(matchRepository, never()).save(any());
        }

        @Test
        void whenMatchCreated_thenIdIsGenerated() {
            when(teamRepository.findById("team-a")).thenReturn(Optional.of(teamEntityA));
            when(teamRepository.findById("team-b")).thenReturn(Optional.of(teamEntityB));
            when(matchRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            Match result = matchService.createMatch(LocalDate.now(), "team-a", "team-b");

            assertNotNull(result.getId());
            assertFalse(result.getId().isBlank());
        }
    }


    @Nested
    class SetReferee {

        @Test
        void whenMatchAndRefereeExist_thenSetsRefereeSuccessfully() {
            RefereeEntity refereeEntity = new RefereeEntity();
            refereeEntity.setId("ref-1");

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(refereeRepository.findById("ref-1")).thenReturn(Optional.of(refereeEntity));
            when(matchRepository.save(any())).thenReturn(matchEntity);

            Match result = matchService.setReferee("match-1", "ref-1");

            assertNotNull(result);
            verify(matchRepository).save(matchEntity);
        }

        @Test
        void whenMatchDoesNotExist_thenThrowsMatchNotFoundException() {
            when(matchRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(MatchNotFoundException.class,
                    () -> matchService.setReferee("unknown", "ref-1"));

            verify(refereeRepository, never()).findById(any());
        }

        @Test
        void whenRefereeDoesNotExist_thenThrowsUserNotFoundException() {
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(refereeRepository.findById("ref-unknown")).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> matchService.setReferee("match-1", "ref-unknown"));

            verify(matchRepository, never()).save(any());
        }
    }



    @Nested
    class AddMatchEventGoal {

        @Test
        void whenMatchAndPlayerExist_thenAddsGoalSuccessfully() {
            Player player = buildPlayer("player-1");

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerService.getPlayerByUserId("player-1")).thenReturn(Optional.of(player));
            when(matchRepository.save(any())).thenReturn(matchEntity);

            Match result = matchService.addMatchEventGoal("match-1", "player-1", 35, "Header goal");

            assertNotNull(result);
            verify(matchRepository).save(matchEntity);
        }

        @Test
        void whenMatchDoesNotExist_thenThrowsMatchNotFoundException() {
            when(matchRepository.findById("unknown")).thenReturn(Optional.empty());

            assertThrows(MatchNotFoundException.class,
                    () -> matchService.addMatchEventGoal("unknown", "player-1", 10, "Goal"));

            verifyNoInteractions(playerService);
        }

        @Test
        void whenPlayerDoesNotExist_thenThrowsUserNotFoundException() {
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerService.getPlayerByUserId("unknown-player")).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> matchService.addMatchEventGoal("match-1", "unknown-player", 10, "Goal"));

            verify(matchRepository, never()).save(any());
        }

        @Test
        void whenGoalAdded_thenGoalIsStoredInMatch() {
            Player player = buildPlayer("player-1");

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerService.getPlayerByUserId("player-1")).thenReturn(Optional.of(player));
            when(matchRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            matchService.addMatchEventGoal("match-1", "player-1", 78, "Long shot");

            assertEquals(1, matchEntity.getGoals().size());
            assertEquals(78, matchEntity.getGoals().get(0).getMinute());
        }
    }



    @Nested
    class AddMatchEventCard {

        @Test
        void cuandoPartidoYJugadorExisten_agregaTarjeta() {
            PlayerEntity playerEntity = buildPlayerEntity("player-1");

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerRepository.findByUserId("player-1")).thenReturn(Optional.of(playerEntity));
            when(matchRepository.save(any())).thenReturn(matchEntity);

            Match result = matchService.addMatchEventCard("match-1", "player-1", 55, CardEntity.CardType.YELLOW, "Falta grave");

            assertNotNull(result);
            assertEquals(1, matchEntity.getCards().size());
            assertEquals(CardEntity.CardType.YELLOW, matchEntity.getCards().get(0).getType());
            verify(matchRepository).save(matchEntity);
        }

        @Test
        void cuandoPartidoNoExiste_lanzaExcepcion() {
            when(matchRepository.findById("nope")).thenReturn(Optional.empty());

            assertThrows(MatchNotFoundException.class,
                    () -> matchService.addMatchEventCard("nope", "p1", 10, CardEntity.CardType.RED, "desc"));
        }

        @Test
        void cuandoJugadorNoExiste_lanzaExcepcion() {
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerRepository.findByUserId("ghost")).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> matchService.addMatchEventCard("match-1", "ghost", 10, CardEntity.CardType.YELLOW, "desc"));

            verify(matchRepository, never()).save(any());
        }
    }

    @Nested
    class FinalizeMatch {

        @Test
        void conResultadoCorrecto_guardaMarcadorYFinaliza() {
            PlayerEntity playerEntity = buildPlayerEntity("p1");
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(playerRepository.findByUserId("p1")).thenReturn(Optional.of(playerEntity));
            when(matchRepository.save(any())).thenReturn(matchEntity);

            PlayerMatchStatsDTO stats = new PlayerMatchStatsDTO("p1", 2, 1, 0);
            MatchResultDTO result = new MatchResultDTO(3, 1, List.of(stats));

            Match match = matchService.finalizeMatch("match-1", result);

            assertEquals("FINISHED", matchEntity.getStatus());
            assertEquals(3, matchEntity.getLocalScore());
            assertEquals(1, matchEntity.getVisitorScore());
            assertNotNull(match);
        }

        @Test
        void sinStats_soloActualizaMarcador() {
            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(matchRepository.save(any())).thenReturn(matchEntity);

            MatchResultDTO result = new MatchResultDTO(0, 0, List.of());

            matchService.finalizeMatch("match-1", result);

            assertEquals("FINISHED", matchEntity.getStatus());
            assertTrue(matchEntity.getGoals().isEmpty());
        }

        @Test
        void cuandoPartidoNoExiste_lanzaExcepcion() {
            when(matchRepository.findById("nope")).thenReturn(Optional.empty());

            assertThrows(MatchNotFoundException.class,
                    () -> matchService.finalizeMatch("nope", new MatchResultDTO(1, 0, List.of())));
        }
    }

    private TeamEntity buildTeamEntity(String id, String name) {
        TeamEntity entity = new TeamEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setFormation(Formation.FORMATION_4_4_2);
        return entity;
    }

    private PlayerEntity buildPlayerEntity(String userId) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(userId);
        entity.setPosition(Position.FORWARD);
        entity.setDorsalNumber(9);
        entity.setStatus(PlayerStatus.AVAILABLE);
        return entity;
    }

    private Player buildPlayer(String userId) {
        UserPlayer userPlayer = new UserPlayer(userId, "Test Player", userId + "@test.com",
                LocalDate.of(1998, 1, 1), Gender.MALE, "Password1");
        Player player = new Player(userPlayer, Position.FORWARD, 9);
        player.setStatus(PlayerStatus.AVAILABLE);
        return player;
    }
}
