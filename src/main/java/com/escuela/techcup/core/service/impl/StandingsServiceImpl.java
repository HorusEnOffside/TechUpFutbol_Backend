package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.model.Card;
import com.escuela.techcup.core.service.StandingsService;
import com.escuela.techcup.persistence.repository.tournament.TeamRepository;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import com.escuela.techcup.persistence.repository.tournament.GoalRepository;
import com.escuela.techcup.persistence.repository.tournament.CardRepository;
import com.escuela.techcup.persistence.mapper.tournament.CardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StandingsServiceImpl implements StandingsService {

    private static final Logger log = LoggerFactory.getLogger(StandingsServiceImpl.class);

    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final GoalRepository goalRepository;
    private final CardRepository cardRepository;

    public StandingsServiceImpl(TeamRepository teamRepository, MatchRepository matchRepository, GoalRepository goalRepository, CardRepository cardRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.goalRepository = goalRepository;
        this.cardRepository = cardRepository;
    }

    @Override

    public void updateStandingsTable(String tournamentId) {
        // No es necesario persistir, se calcula en tiempo real.
    }

    @Override
    public List<Team> getStandingsTable(String tournamentId) {
        var equipos = teamRepository.findAll().stream()
                .filter(t -> t.getTournament() != null && t.getTournament().getId().equals(tournamentId))
                .toList();


        var partidos = matchRepository.findAll().stream()
                .filter(m -> m.getTournament() != null && m.getTournament().getId().equals(tournamentId))
                .toList();


        class Stats {
            int pj = 0, pg = 0, pe = 0, pp = 0, gf = 0, gc = 0;
            int getDG() { return gf - gc; }
            int getPts() { return pg * 3 + pe; }
        }
        var statsMap = new java.util.HashMap<String, Stats>();
        for (var equipo : equipos) {
            statsMap.put(equipo.getId(), new Stats());
        }


        for (var partido : partidos) {
            var teamA = partido.getTeamA();
            var teamB = partido.getTeamB();
            if (teamA == null || teamB == null) continue;
            var golesA = partido.getGoals().stream().filter(g -> g.getPlayer().getTeam() != null && g.getPlayer().getTeam().getId().equals(teamA.getId())).count();
            var golesB = partido.getGoals().stream().filter(g -> g.getPlayer().getTeam() != null && g.getPlayer().getTeam().getId().equals(teamB.getId())).count();

            Stats statsA = statsMap.get(teamA.getId());
            Stats statsB = statsMap.get(teamB.getId());
            if (statsA == null || statsB == null) continue;
            statsA.pj++;
            statsB.pj++;
            statsA.gf += golesA;
            statsA.gc += golesB;
            statsB.gf += golesB;
            statsB.gc += golesA;
            if (golesA > golesB) {
                statsA.pg++;
                statsB.pp++;
            } else if (golesA < golesB) {
                statsB.pg++;
                statsA.pp++;
            } else {
                statsA.pe++;
                statsB.pe++;
            }
        }


        var result = new java.util.ArrayList<Team>();
        for (var equipo : equipos) {
            Team teamModel = com.escuela.techcup.persistence.mapper.tournament.TeamMapper.toModel(equipo);
            result.add(teamModel);
        }


        result.sort((t1, t2) -> {
            Stats s1 = statsMap.get(t1.getId());
            Stats s2 = statsMap.get(t2.getId());
            int cmp = Integer.compare(s2.getPts(), s1.getPts());
            if (cmp == 0) {
                cmp = Integer.compare(s2.getDG(), s1.getDG());
            }
            return cmp;
        });
        return result;
    }

    @Override
    public List<Player> getTopScorers(String tournamentId) {
        var partidos = matchRepository.findAll().stream()
                .filter(m -> m.getTournament() != null && m.getTournament().getId().equals(tournamentId))
                .toList();
        var goles = new java.util.ArrayList<com.escuela.techcup.persistence.entity.tournament.GoalEntity>();
        for (var partido : partidos) {
            goles.addAll(partido.getGoals());
        }
        var golesPorJugador = new java.util.HashMap<String, Integer>();
        var jugadorEntidadMap = new java.util.HashMap<String, com.escuela.techcup.persistence.entity.users.PlayerEntity>();
        for (var gol : goles) {
            if (gol.getPlayer() == null) continue;
            String playerId = gol.getPlayer().getId();
            golesPorJugador.put(playerId, golesPorJugador.getOrDefault(playerId, 0) + 1);
            jugadorEntidadMap.put(playerId, gol.getPlayer());
        }
        var lista = new java.util.ArrayList<Player>();
        for (var entry : golesPorJugador.entrySet()) {
            var player = com.escuela.techcup.persistence.mapper.users.PlayerMapper.toModel(jugadorEntidadMap.get(entry.getKey()));
            lista.add(player);
        }
        lista.sort((p1, p2) -> {
            int cmp = Integer.compare(golesPorJugador.get(p2.getUserId()), golesPorJugador.get(p1.getUserId()));
            if (cmp == 0) {
                cmp = p1.getName().compareToIgnoreCase(p2.getName());
            }
            return cmp;
        });
        return lista;
    }

    @Override
    public List<MatchEvent> getCardsHistory(String tournamentId, String playerOrTeamId) {
        List<com.escuela.techcup.persistence.entity.tournament.CardEntity> cardEntities;
        if (playerOrTeamId == null || playerOrTeamId.isEmpty()) {
            cardEntities = cardRepository.findByPlayer_Team_Tournament_Id(tournamentId);
        } else {
            cardEntities = cardRepository.findByPlayer_Team_Tournament_IdAndPlayer_Id(tournamentId, playerOrTeamId);
            if (cardEntities.isEmpty()) {
                cardEntities = cardRepository.findByPlayer_Team_Tournament_IdAndPlayer_Team_Id(tournamentId, playerOrTeamId);
            }
        }
        return cardEntities.stream().map(CardMapper::toModel).map(c -> (MatchEvent) c).toList();
    }
}
