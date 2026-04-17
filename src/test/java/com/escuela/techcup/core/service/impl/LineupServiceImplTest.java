package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.LineupPlayerDTO;
import com.escuela.techcup.controller.dto.LineupRequestDTO;
import com.escuela.techcup.controller.dto.LineupResponseDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.LineupEntity;
import com.escuela.techcup.persistence.entity.tournament.LineupPlayerEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.tournament.LineupRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LineupServiceImplTest {

    @Mock private LineupRepository lineupRepository;
    @Mock private MatchRepository matchRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private PlayerRepository playerRepository;

    @InjectMocks private LineupServiceImpl lineupService;

    private MatchEntity matchEntity;
    private TeamEntity teamEntity;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        matchEntity = new MatchEntity();
        matchEntity.setId("match-1");

        teamEntity = new TeamEntity();
        teamEntity.setId("team-1");
        teamEntity.setName("Los Rojos");

        UserPlayerEntity userPlayer = new UserPlayerEntity();
        userPlayer.setId("p1");
        userPlayer.setName("Carlos Ruiz");

        playerEntity = new PlayerEntity();
        playerEntity.setId("p1");
        playerEntity.setUser(userPlayer);
        playerEntity.setPosition(Position.FORWARD);
        playerEntity.setDorsalNumber(9);
    }

    @Nested
    class SubmitLineup {

        @Test
        void alineacionValida_seGuardaCorrectamente() {
            LineupRequestDTO request = buildRequest(List.of(
                    new LineupPlayerDTO("p1", Position.FORWARD, 9)
            ));

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(lineupRepository.existsByMatchIdAndTeamId("match-1", "team-1")).thenReturn(false);
            when(playerRepository.findByUserId("p1")).thenReturn(Optional.of(playerEntity));
            when(lineupRepository.save(any())).thenAnswer(i -> {
                LineupEntity saved = i.getArgument(0);
                saved.setMatch(matchEntity);
                saved.setTeam(teamEntity);
                return saved;
            });

            LineupResponseDTO response = lineupService.submitLineup(request);

            assertNotNull(response);
            assertEquals("match-1", response.getMatchId());
            assertEquals("team-1", response.getTeamId());
            assertEquals("SUBMITTED", response.getStatus());
            assertEquals(Formation.FORMATION_4_4_2, response.getFormation());
            verify(lineupRepository).save(any(LineupEntity.class));
        }

        @Test
        void siYaExisteAlineacion_lanzaExcepcion() {
            LineupRequestDTO request = buildRequest(List.of(
                    new LineupPlayerDTO("p1", Position.FORWARD, 9)
            ));

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(lineupRepository.existsByMatchIdAndTeamId("match-1", "team-1")).thenReturn(true);

            assertThrows(InvalidInputException.class, () -> lineupService.submitLineup(request));
            verify(lineupRepository, never()).save(any());
        }

        @Test
        void sinJugadores_lanzaExcepcion() {
            LineupRequestDTO request = buildRequest(List.of());

            assertThrows(InvalidInputException.class, () -> lineupService.submitLineup(request));
            verifyNoInteractions(matchRepository);
        }

        @Test
        void jugadorNoEncontrado_lanzaExcepcion() {
            LineupRequestDTO request = buildRequest(List.of(
                    new LineupPlayerDTO("noExiste", Position.FORWARD, 9)
            ));

            when(matchRepository.findById("match-1")).thenReturn(Optional.of(matchEntity));
            when(teamRepository.findById("team-1")).thenReturn(Optional.of(teamEntity));
            when(lineupRepository.existsByMatchIdAndTeamId("match-1", "team-1")).thenReturn(false);
            when(playerRepository.findByUserId("noExiste")).thenReturn(Optional.empty());

            assertThrows(InvalidInputException.class, () -> lineupService.submitLineup(request));
            verify(lineupRepository, never()).save(any());
        }

        @Test
        void partidoNoEncontrado_lanzaExcepcion() {
            LineupRequestDTO request = buildRequest(List.of(
                    new LineupPlayerDTO("p1", Position.FORWARD, 9)
            ));
            when(matchRepository.findById("match-1")).thenReturn(Optional.empty());

            assertThrows(Exception.class, () -> lineupService.submitLineup(request));
        }

        private LineupRequestDTO buildRequest(List<LineupPlayerDTO> players) {
            return new LineupRequestDTO("match-1", "team-1", Formation.FORMATION_4_4_2, players);
        }
    }

    @Nested
    class GetLineup {

        @Test
        void cuandoExiste_devuelveAlineacion() {
            LineupEntity lineup = buildLineupEntity("lineup-1", 1);

            when(lineupRepository.findByMatchIdAndTeamId("match-1", "team-1"))
                    .thenReturn(Optional.of(lineup));

            LineupResponseDTO response = lineupService.getLineup("match-1", "team-1");

            assertEquals("lineup-1", response.getId());
            assertEquals("SUBMITTED", response.getStatus());
        }

        @Test
        void cuandoNoExiste_lanzaExcepcion() {
            when(lineupRepository.findByMatchIdAndTeamId("match-1", "team-1"))
                    .thenReturn(Optional.empty());

            assertThrows(InvalidInputException.class,
                    () -> lineupService.getLineup("match-1", "team-1"));
        }
    }

    @Nested
    class ValidateLineup {

        @Test
        void alineacionConPorteroYSuficientesJugadores_pasaValidacion() {
            LineupEntity lineup = buildLineupEntity("lineup-1", 8);

            when(lineupRepository.findById("lineup-1")).thenReturn(Optional.of(lineup));
            when(lineupRepository.save(any())).thenAnswer(i -> i.getArgument(0));

            LineupResponseDTO response = lineupService.validateLineup("lineup-1");

            assertEquals("VALIDATED", response.getStatus());
        }

        @Test
        void menosDeSetePLayers_lanzaExcepcion() {
            LineupEntity lineup = buildLineupEntity("lineup-1", 5);

            when(lineupRepository.findById("lineup-1")).thenReturn(Optional.of(lineup));

            assertThrows(InvalidInputException.class,
                    () -> lineupService.validateLineup("lineup-1"));
        }

        @Test
        void sinPortero_lanzaExcepcion() {
            LineupEntity lineup = buildLineupEntitySinPortero("lineup-1", 8);

            when(lineupRepository.findById("lineup-1")).thenReturn(Optional.of(lineup));

            assertThrows(InvalidInputException.class,
                    () -> lineupService.validateLineup("lineup-1"));
        }

        @Test
        void alineacionNoEncontrada_lanzaExcepcion() {
            when(lineupRepository.findById("nope")).thenReturn(Optional.empty());

            assertThrows(InvalidInputException.class,
                    () -> lineupService.validateLineup("nope"));
        }
    }

    private LineupEntity buildLineupEntity(String id, int numPlayers) {
        LineupEntity lineup = new LineupEntity();
        lineup.setId(id);
        lineup.setMatch(matchEntity);
        lineup.setTeam(teamEntity);
        lineup.setFormation(Formation.FORMATION_4_4_2);
        lineup.setStatus("SUBMITTED");

        List<LineupPlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            LineupPlayerEntity lp = new LineupPlayerEntity();
            lp.setId("lp-" + i);
            lp.setLineup(lineup);
            lp.setPlayer(playerEntity);
            lp.setPosition(i == 0 ? Position.GOALKEEPER : Position.MIDFIELDER);
            lp.setDorsalNumber(i + 1);
            players.add(lp);
        }
        lineup.setPlayers(players);
        return lineup;
    }

    private LineupEntity buildLineupEntitySinPortero(String id, int numPlayers) {
        LineupEntity lineup = new LineupEntity();
        lineup.setId(id);
        lineup.setMatch(matchEntity);
        lineup.setTeam(teamEntity);
        lineup.setFormation(Formation.FORMATION_4_4_2);
        lineup.setStatus("SUBMITTED");

        List<LineupPlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            LineupPlayerEntity lp = new LineupPlayerEntity();
            lp.setId("lp-" + i);
            lp.setLineup(lineup);
            lp.setPlayer(playerEntity);
            lp.setPosition(Position.MIDFIELDER);
            lp.setDorsalNumber(i + 1);
            players.add(lp);
        }
        lineup.setPlayers(players);
        return lineup;
    }
}
