package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    @Test
    void toModel_returnsCorrectPosition() {
        assertEquals(Position.FORWARD, PlayerMapper.toModel(buildEntity()).getPosition());
    }

    @Test
    void toModel_returnsCorrectDorsal() {
        assertEquals(9, PlayerMapper.toModel(buildEntity()).getDorsalNumber());
    }

    @Test
    void toModel_returnsCorrectStatus() {
        assertEquals(PlayerStatus.AVAILABLE, PlayerMapper.toModel(buildEntity()).getStatus());
    }

    @Test
    void toModel_withNull_returnsNull() {
        assertNull(PlayerMapper.toModel(null));
    }

    @Test
    void toEntity_returnsCorrectPosition() {
        assertEquals(Position.FORWARD, PlayerMapper.toEntity(buildModel()).getPosition());
    }

    @Test
    void toEntity_returnsCorrectDorsal() {
        assertEquals(9, PlayerMapper.toEntity(buildModel()).getDorsalNumber());
    }

    @Test
    void toEntity_withNull_returnsNull() {
        assertNull(PlayerMapper.toEntity(null));
    }

    private PlayerEntity buildEntity() {
        UserPlayerEntity up = new UserPlayerEntity();
        up.setId("u1");
        up.setName("Pedro");
        up.setMail("pedro@test.com");
        up.setDateOfBirth(LocalDate.of(2000, 1, 1));
        up.setGender(Gender.MALE);
        up.setPasswordHash("hash");

        PlayerEntity e = new PlayerEntity();
        e.setId("u1");
        e.setUser(up);
        e.setPosition(Position.FORWARD);
        e.setDorsalNumber(9);
        e.setStatus(PlayerStatus.AVAILABLE);
        return e;
    }

    private Player buildModel() {
        UserPlayer up = new UserPlayer("u1", "Pedro", "pedro@test.com",
                LocalDate.of(2000, 1, 1), Gender.MALE, "hash");
        return new Player(up, Position.FORWARD, 9);
    }
}