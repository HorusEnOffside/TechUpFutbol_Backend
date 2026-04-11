package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.mapper.tournament.GoalMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GoalMapperTest {

    private PlayerEntity buildPlayerEntity() {
        UserPlayerEntity user = new UserPlayerEntity();
        user.setId("user-1");
        user.setName("Jugador");
        user.setMail("jugador@test.com");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setGender(Gender.MALE);
        user.setPasswordHash("hash");

        PlayerEntity player = new PlayerEntity();
        player.setId("player-1");
        player.setUser(user);
        player.setPosition(com.escuela.techcup.core.model.enums.Position.FORWARD);
        player.setDorsalNumber(9);
        return player;
    }

    private Player buildPlayerModel() {
        UserPlayer user = new UserPlayer("user-1", "Jugador", "jugador@test.com",
                LocalDate.of(2000, 1, 1), Gender.MALE, "hash");
        return new Player(user, Position.FORWARD, 9);
    }

    @Test
    void toEntity_mapsFieldsCorrectly() {
        Goal goal = new Goal("goal-1", 35, buildPlayerModel(), "Gol de cabeza");

        GoalEntity entity = GoalMapper.toEntity(goal);

        assertNotNull(entity);
        assertEquals("goal-1", entity.getId());
        assertEquals(35, entity.getMinute());
        assertEquals("Gol de cabeza", entity.getDescription());
        assertNotNull(entity.getPlayer());
    }

    @Test
    void toModel_mapsFieldsCorrectly() {
        GoalEntity entity = new GoalEntity();
        entity.setId("goal-2");
        entity.setMinute(70);
        entity.setPlayer(buildPlayerEntity());
        entity.setDescription("Gol de penal");

        Goal model = GoalMapper.toModel(entity);

        assertNotNull(model);
        assertEquals("goal-2", model.getId());
        assertEquals(70, model.getMinute());
        assertEquals("Gol de penal", model.getDescription());
        assertNotNull(model.getPlayer());
    }

    @Test
    void roundtrip_toEntityThenToModel_preservesData() {
        Goal original = new Goal("goal-3", 10, buildPlayerModel(), "desc");
        Goal result = GoalMapper.toModel(GoalMapper.toEntity(original));

        assertEquals(original.getId(), result.getId());
        assertEquals(original.getMinute(), result.getMinute());
        assertEquals(original.getDescription(), result.getDescription());
    }
}