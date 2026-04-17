package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;
import java.util.UUID;

public class GoalMapper {

    private GoalMapper() {
    }

    public static GoalEntity toEntity(Goal goal) {
        GoalEntity entity = new GoalEntity();
        entity.setId(UUID.fromString(goal.getId()));
        entity.setMinute(goal.getMinute());
        entity.setPlayer(PlayerMapper.toEntity(goal.getPlayer()));
        entity.setDescription(goal.getDescription());
        return entity;
    }

    public static Goal toModel(GoalEntity entity) {
        return new Goal(entity.getId().toString(), entity.getMinute(), PlayerMapper.toModel(entity.getPlayer()), entity.getDescription());
    }

}
