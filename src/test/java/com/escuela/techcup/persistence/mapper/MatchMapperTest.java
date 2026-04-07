package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Match;
import com.escuela.techcup.core.model.MatchEvent;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.Goal;
import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.GoalEntity;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchMapperTest {

    private static final LocalDateTime DATE_TIME = LocalDateTime.of(2025, 5, 10, 18, 0);


    // -------------------------------------------------------------------------
    // toEntity
    // -------------------------------------------------------------------------

    @Nested
    class ToEntity {

        @Test
        void whenNullModel_thenReturnsNull() {
            assertNull(MatchMapper.toEntity(null));
        }

        @Test
        void whenMinimalModel_thenMapsBaseFields() {
            Match match = buildMinimalMatch();

            MatchEntity result = MatchMapper.toEntity(match);

            assertNotNull(result);
            assertEquals("match-1", result.getId());
            assertEquals(DATE_TIME, result.getDateTime());
        }

        @Test
        void whenModelHasTeams_thenTeamsAreMapped() {
            Match match = buildMinimalMatch();

            MatchEntity result = MatchMapper.toEntity(match);

            assertNotNull(result.getTeamA());
            assertNotNull(result.getTeamB());
            assertEquals("team-a", result.getTeamA().getId());
            assertEquals("team-b", result.getTeamB().getId());
        }

        @Test
        void whenModelHasNullTeams_thenEntityTeamsAreNull() {
            Match match = new Match("match-1", DATE_TIME, null, null);

            MatchEntity result = MatchMapper.toEntity(match);

            assertNull(result.getTeamA());
            assertNull(result.getTeamB());
        }

        @Test
        void whenModelHasNullReferee_thenEntityRefereeIsNull() {
            Match match = buildMinimalMatch();

            MatchEntity result = MatchMapper.toEntity(match);

            assertNull(result.getReferee());
        }

        @Test
        void whenModelHasNullSoccerField_thenEntitySoccerFieldIsNull() {
            Match match = buildMinimalMatch();

            MatchEntity result = MatchMapper.toEntity(match);

            assertNull(result.getSoccerField());
        }

        @Test
        void whenModelHasNullEvents_thenEntityGoalsIsEmpty() {
            Match match = buildMinimalMatch();
            match.setEvents(null);

            MatchEntity result = MatchMapper.toEntity(match);

            assertNotNull(result.getGoals());
            assertTrue(result.getGoals().isEmpty());
        }

        @Test
        void whenModelHasGoalEvents_thenGoalsAreMapped() {
            Match match = buildMinimalMatch();
            Goal goal = new Goal("goal-1", 30, buildPlayer("p-1"), "Header goal");
            match.setEvents(List.of(goal));

            MatchEntity result = MatchMapper.toEntity(match);

            assertNotNull(result.getGoals());
            assertEquals(1, result.getGoals().size());
            assertEquals("goal-1", result.getGoals().get(0).getId());
            assertEquals(30, result.getGoals().get(0).getMinute());
        }

        @Test
        void whenGoalEntityHasMatchBackReference_thenIsSetCorrectly() {
            Match match = buildMinimalMatch();
            Goal goal = new Goal("goal-1", 45, buildPlayer("p-1"), "Penalty");
            match.setEvents(List.of(goal));

            MatchEntity result = MatchMapper.toEntity(match);

            assertSame(result, result.getGoals().get(0).getMatch());
        }
    }


    // -------------------------------------------------------------------------
    // toModel
    // -------------------------------------------------------------------------

    @Nested
    class ToModel {

        @Test
        void whenNullEntity_thenReturnsNull() {
            assertNull(MatchMapper.toModel(null));
        }

        @Test
        void whenMinimalEntity_thenMapsBaseFields() {
            MatchEntity entity = buildMinimalMatchEntity();

            Match result = MatchMapper.toModel(entity);

            assertNotNull(result);
            assertEquals("match-1", result.getId());
            assertEquals(DATE_TIME, result.getDateTime());
        }

        @Test
        void whenEntityHasTeams_thenTeamsAreMapped() {
            MatchEntity entity = buildMinimalMatchEntity();

            Match result = MatchMapper.toModel(entity);

            assertNotNull(result.getTeamA());
            assertNotNull(result.getTeamB());
            assertEquals("team-a", result.getTeamA().getId());
            assertEquals("team-b", result.getTeamB().getId());
        }

        @Test
        void whenEntityHasNullTeams_thenModelTeamsAreNull() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setTeamA(null);
            entity.setTeamB(null);

            Match result = MatchMapper.toModel(entity);

            assertNull(result.getTeamA());
            assertNull(result.getTeamB());
        }

        @Test
        void whenEntityHasNullReferee_thenModelRefereeIsNull() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setReferee(null);

            Match result = MatchMapper.toModel(entity);

            assertNull(result.getReferee());
        }

        @Test
        void whenEntityHasNullSoccerField_thenModelSoccerFieldIsNull() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setSoccerField(null);

            Match result = MatchMapper.toModel(entity);

            assertNull(result.getSoccerField());
        }

        @Test
        void whenEntityHasNullGoals_thenModelEventsIsNull() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setGoals(null);

            Match result = MatchMapper.toModel(entity);

            assertNull(result.getEvents());
        }

        @Test
        void whenEntityHasEmptyGoals_thenModelEventsIsEmpty() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setGoals(new ArrayList<>());

            Match result = MatchMapper.toModel(entity);

            assertNotNull(result.getEvents());
            assertTrue(result.getEvents().isEmpty());
        }

        @Test
        void whenEntityHasGoals_thenEventsAreMappedAsGoals() {
            MatchEntity entity = buildMinimalMatchEntity();
            GoalEntity goalEntity = buildGoalEntity("goal-1", 55, "Free kick");
            entity.setGoals(List.of(goalEntity));

            Match result = MatchMapper.toModel(entity);

            assertNotNull(result.getEvents());
            assertEquals(1, result.getEvents().size());
            MatchEvent event = result.getEvents().get(0);
            assertInstanceOf(Goal.class, event);
            assertEquals("goal-1", ((Goal) event).getId());
            assertEquals(55, ((Goal) event).getMinute());
            assertEquals("Free kick", ((Goal) event).getDescription());
        }

        @Test
        void whenEntityHasMultipleGoals_thenAllAreMapped() {
            MatchEntity entity = buildMinimalMatchEntity();
            entity.setGoals(List.of(
                    buildGoalEntity("goal-1", 10, "Corner"),
                    buildGoalEntity("goal-2", 78, "Counter attack")
            ));

            Match result = MatchMapper.toModel(entity);

            assertEquals(2, result.getEvents().size());
        }
    }


    // -------------------------------------------------------------------------
    // roundtrip
    // -------------------------------------------------------------------------

    @Nested
    class Roundtrip {

        @Test
        void modelToEntityToModel_preservesBaseFields() {
            Match original = buildMinimalMatch();

            Match result = MatchMapper.toModel(MatchMapper.toEntity(original));

            assertEquals(original.getId(), result.getId());
            assertEquals(original.getDateTime(), result.getDateTime());
            assertEquals(original.getTeamA().getId(), result.getTeamA().getId());
            assertEquals(original.getTeamB().getId(), result.getTeamB().getId());
        }

        @Test
        void modelWithGoals_toEntityToModel_preservesGoals() {
            Match original = buildMinimalMatch();
            Goal goal = new Goal("goal-1", 22, buildPlayer("p-1"), "Volley");
            original.setEvents(List.of(goal));

            Match result = MatchMapper.toModel(MatchMapper.toEntity(original));

            assertNotNull(result.getEvents());
            assertEquals(1, result.getEvents().size());
            assertEquals("goal-1", ((Goal) result.getEvents().get(0)).getId());
        }
    }


    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private Match buildMinimalMatch() {
        Team teamA = new Team("team-a", "Alpha", null, null, Formation.FORMATION_4_4_2);
        Team teamB = new Team("team-b", "Beta", null, null, Formation.FORMATION_4_4_2);
        return new Match("match-1", DATE_TIME, teamA, teamB);
    }

    private MatchEntity buildMinimalMatchEntity() {
        TeamEntity teamA = new TeamEntity();
        teamA.setId("team-a");
        teamA.setName("Alpha");
        teamA.setFormation(Formation.FORMATION_4_4_2);

        TeamEntity teamB = new TeamEntity();
        teamB.setId("team-b");
        teamB.setName("Beta");
        teamB.setFormation(Formation.FORMATION_4_4_2);

        MatchEntity entity = new MatchEntity();
        entity.setId("match-1");
        entity.setDateTime(DATE_TIME);
        entity.setTeamA(teamA);
        entity.setTeamB(teamB);
        entity.setGoals(new ArrayList<>());
        return entity;
    }

    private GoalEntity buildGoalEntity(String id, int minute, String description) {
        UserPlayerEntity userEntity = new UserPlayerEntity();
        userEntity.setId("p-1");
        userEntity.setName("Test Player");
        userEntity.setMail("p1@test.com");
        userEntity.setDateOfBirth(LocalDate.of(1998, 1, 1));
        userEntity.setGender(Gender.MALE);
        userEntity.setPasswordHash("Password1");

        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setId("p-1");
        playerEntity.setUser(userEntity);
        playerEntity.setPosition(Position.FORWARD);
        playerEntity.setDorsalNumber(9);
        playerEntity.setStatus(PlayerStatus.AVAILABLE);

        GoalEntity goalEntity = new GoalEntity();
        goalEntity.setId(id);
        goalEntity.setMinute(minute);
        goalEntity.setDescription(description);
        goalEntity.setPlayer(playerEntity);
        return goalEntity;
    }

    private Player buildPlayer(String userId) {
        UserPlayer userPlayer = new UserPlayer(userId, "Test Player", userId + "@test.com",
                LocalDate.of(1998, 1, 1), Gender.MALE, "Password1");
        Player player = new Player(userPlayer, Position.FORWARD, 9);
        player.setStatus(PlayerStatus.AVAILABLE);
        return player;
    }
}
