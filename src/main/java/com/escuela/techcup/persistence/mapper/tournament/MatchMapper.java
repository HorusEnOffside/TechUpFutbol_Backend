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

public class MatchMapper {
    private MatchMapper() {
    }



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
        if (match.getEvents() != null) {
            List<GoalEntity> goalEntities = new ArrayList<>();
            for (MatchEvent event : match.getEvents()) {
                if (event instanceof Goal) {
                    GoalEntity ge = GoalMapper.toEntity((Goal) event);
                    ge.setMatch(matchEntity); // set back-reference
                    goalEntities.add(ge);
                }
            }
            matchEntity.setGoals(goalEntities);
        }
        return matchEntity;
    }


    public static Match toModel(MatchEntity matchEntity) {
        if (matchEntity == null) return null;
        Team teamA = matchEntity.getTeamA() != null ? TeamMapper.toModel(matchEntity.getTeamA()) : null;
        Team teamB = matchEntity.getTeamB() != null ? TeamMapper.toModel(matchEntity.getTeamB()) : null;
        Referee referee = matchEntity.getReferee() != null ? RefereeMapper.toModel(matchEntity.getReferee()) : null;
        SoccerField soccerField = matchEntity.getSoccerField() != null ? SoccerFieldMapper.toModel(matchEntity.getSoccerField()) : null;
        List<MatchEvent> events = null;
        if (matchEntity.getGoals() != null) {
            events = new ArrayList<>();
            for (GoalEntity ge : matchEntity.getGoals()) {
                events.add(GoalMapper.toModel(ge));
            }
        }
        return new Match(
                matchEntity.getId(),
                matchEntity.getDateTime(),
                teamA,
                teamB,
                referee,
                soccerField,
                events
        );
    }

}
