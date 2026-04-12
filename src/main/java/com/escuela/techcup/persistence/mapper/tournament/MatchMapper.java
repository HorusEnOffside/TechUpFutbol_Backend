package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.Referee;
import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.persistence.mapper.users.RefereeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatchMapper {
    private MatchMapper() {}

    public static MatchEntity toEntity(Match match) {
        if (match == null) return null;
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(match.getId());
        matchEntity.setDateTime(match.getDateTime());
        if (match.getTeamA() != null) {
            matchEntity.setTeamA(TeamMapper.toEntity(match.getTeamA()));
        }
        if (match.getTeamB() != null) {
            matchEntity.setTeamB(TeamMapper.toEntity(match.getTeamB()));
        }
        if (match.getReferee() != null) {
            matchEntity.setReferee(RefereeMapper.toEntity(match.getReferee()));
        }
        if (match.getSoccerField() != null) {
            matchEntity.setSoccerField(SoccerFieldMapper.toEntity(match.getSoccerField()));
        }
        matchEntity.setStatus(match.getStatus());


        List<MatchEvent> events = match.getEvents();
        if (events != null) {
            List<GoalEntity> goals = events.stream()
                    .filter(e -> e instanceof Goal)
                    .map(e -> {
                        GoalEntity goalEntity = GoalMapper.toEntity((Goal) e);
                        // Set back reference
                        goalEntity.setMatch(matchEntity);
                        return goalEntity;
                    })
                    .collect(Collectors.toList());
            matchEntity.setGoals(goals);
        } else {
            matchEntity.setGoals(new ArrayList<>());
        }

        return matchEntity;
    }

    public static Match toModel(MatchEntity matchEntity) {
        if (matchEntity == null) return null;
        Team teamA = matchEntity.getTeamA() != null ? TeamMapper.toModel(matchEntity.getTeamA()) : null;
        Team teamB = matchEntity.getTeamB() != null ? TeamMapper.toModel(matchEntity.getTeamB()) : null;
        Referee referee = matchEntity.getReferee() != null ? RefereeMapper.toModel(matchEntity.getReferee()) : null;
        SoccerField soccerField = matchEntity.getSoccerField() != null ? SoccerFieldMapper.toModel(matchEntity.getSoccerField()) : null;
        String status = matchEntity.getStatus();


        List<GoalEntity> goals = matchEntity.getGoals();
        List<MatchEvent> events = null;
        if (goals != null) {
            events = goals.stream()
                    .map(GoalMapper::toModel)
                    .collect(Collectors.toList());
        }


        return new Match(
                matchEntity.getId(),
                matchEntity.getDateTime(),
                teamA,
                teamB,
                referee,
                soccerField,
                events,
                status
        );
    }
}
