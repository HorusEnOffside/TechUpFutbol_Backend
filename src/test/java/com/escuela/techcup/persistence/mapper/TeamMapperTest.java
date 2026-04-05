package com.escuela.techcup.persistence.mapper;

import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamPlayerEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeamMapperTest {

    @Nested
    class ToEntity {

        @Test
        void whenNullModel_thenReturnsNull() {
            assertNull(TeamMapper.toEntity(null));
        }

        @Test
        void whenMinimalModel_thenMapsBaseFields() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNotNull(result);
            assertEquals("team-1", result.getId());
            assertEquals("Los Tigres", result.getName());
            assertEquals(Formation.FORMATION_BASIC, result.getFormation());
        }

        @Test
        void whenModelHasColor_thenMapsColorCorrectly() {
            Team team = new Team("team-1", "Los Tigres", "Rojo", null, Formation.FORMATION_BASIC);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNotNull(result.getUniformColor());
            assertEquals("Rojo", result.getUniformColor());
        }

        @Test
        void whenModelHasNullColor_thenEntityColorIsNull() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNull(result.getUniformColor());
        }

        @Test
        void whenModelHasNullLogo_thenEntityLogoIsEmptyBytes() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNotNull(result.getLogo());
            assertEquals(0, result.getLogo().length);
        }

        @Test
        void whenModelHasNullCaptain_thenEntityCaptainIsNull() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);
            team.setCaptain(null);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNull(result.getCaptainPlayer());
        }

        @Test
        void whenModelHasNullPlayers_thenEntityPlayersIsNull() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);
            team.setPlayers(null);

            TeamEntity result = TeamMapper.toEntity(team);

            assertNull(result.getPlayers());
        }

        @Test
        void whenModelHasPlayers_thenEntityPlayersAreMapped() {
            Team team = new Team("team-1", "Los Tigres", null, null, Formation.FORMATION_BASIC);
            team.setPlayers(List.of(buildPlayer("p-1", Position.FORWARD, 9)));

            TeamEntity result = TeamMapper.toEntity(team);

            assertNotNull(result.getPlayers());
            assertEquals(1, result.getPlayers().size());
        }
    }

    @Nested
    class ToModel {

        @Test
        void whenNullEntity_thenReturnsNull() {
            assertNull(TeamMapper.toModel(null));
        }

        @Test
        void whenMinimalEntity_thenMapsBaseFields() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");

            Team result = TeamMapper.toModel(entity);

            assertNotNull(result);
            assertEquals("team-1", result.getId());
            assertEquals("Los Tigres", result.getName());
            assertEquals(Formation.FORMATION_BASIC, result.getFormation());
        }

        @Test
        void whenEntityHasColor_thenMapsColorToString() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            entity.setUniformColor("Rojo");

            Team result = TeamMapper.toModel(entity);

            assertNotNull(result.getUniformColor());
            assertEquals("Rojo", result.getUniformColor());
        }

        @Test
        void whenEntityHasNullColor_thenModelColorIsNull() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            entity.setUniformColor(null);

            Team result = TeamMapper.toModel(entity);

            assertNull(result.getUniformColor());
        }

        @Test
        void whenEntityHasNullLogo_thenModelLogoIsNull() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            entity.setLogo(null);

            Team result = TeamMapper.toModel(entity);

            assertNull(result.getLogo());
        }

        @Test
        void whenEntityHasNullCaptain_thenModelCaptainIsNull() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            entity.setCaptainPlayer(null);

            Team result = TeamMapper.toModel(entity);

            assertNull(result.getCaptain());
        }

        @Test
        void whenEntityHasNullPlayers_thenModelPlayersIsNull() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            entity.setPlayers(null);

            Team result = TeamMapper.toModel(entity);

            assertNull(result.getPlayers());
        }

        @Test
        void whenEntityHasPlayers_thenModelPlayersAreMapped() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            TeamPlayerEntity tpe = new TeamPlayerEntity();
            tpe.setPlayer(buildPlayerEntity("p-1", Position.DEFENDER, 4));
            tpe.setTeam(entity);
            entity.setPlayers(List.of(tpe));

            Team result = TeamMapper.toModel(entity);

            assertNotNull(result.getPlayers());
            assertEquals(1, result.getPlayers().size());
            assertEquals(4, result.getPlayers().get(0).getDorsalNumber());
        }

        @Test
        void whenEntityHasPlayerWithNullPlayer_thenSkipsIt() {
            TeamEntity entity = buildTeamEntity("team-1", "Los Tigres");
            TeamPlayerEntity tpe = new TeamPlayerEntity();
            tpe.setPlayer(null);
            tpe.setTeam(entity);
            entity.setPlayers(List.of(tpe));

            Team result = TeamMapper.toModel(entity);

            assertNotNull(result.getPlayers());
            assertTrue(result.getPlayers().isEmpty());
        }
    }

    @Nested
    class Roundtrip {

        @Test
        void modelToEntityToModel_preservesBaseFields() {
            Team original = new Team("team-1", "Los Tigres", "Azul", null, Formation.FORMATION_4_4_2);
            original.setCaptain(null);
            original.setPlayers(null);

            Team result = TeamMapper.toModel(TeamMapper.toEntity(original));

            assertEquals(original.getId(), result.getId());
            assertEquals(original.getName(), result.getName());
            assertEquals(original.getFormation(), result.getFormation());
            assertNotNull(result.getUniformColor());
        }
    }

    private TeamEntity buildTeamEntity(String id, String name) {
        TeamEntity entity = new TeamEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setFormation(Formation.FORMATION_BASIC);
        return entity;
    }

    private PlayerEntity buildPlayerEntity(String userId, Position position, int dorsal) {
        UserPlayerEntity userEntity = new UserPlayerEntity();
        userEntity.setId(userId);
        userEntity.setName("Player " + userId);
        userEntity.setMail(userId + "@test.com");
        userEntity.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userEntity.setGender(Gender.MALE);
        userEntity.setPasswordHash("Password1");

        PlayerEntity entity = new PlayerEntity();
        entity.setId(userId);
        entity.setUser(userEntity);
        entity.setPosition(position);
        entity.setDorsalNumber(dorsal);
        entity.setStatus(PlayerStatus.AVAILABLE);
        return entity;
    }

    private Player buildPlayer(String userId, Position position, int dorsal) {
        UserPlayer userPlayer = new UserPlayer(userId, "Player " + userId, userId + "@test.com",
                LocalDate.of(2000, 1, 1), Gender.MALE, "Password1");
        Player player = new Player(userPlayer, position, dorsal);
        player.setStatus(PlayerStatus.AVAILABLE);
        return player;
    }
}