package com.escuela.techcup.persistence.entity.tournament;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class MatchEntityTest {

    @Test
    void camposBasicosSeGuardanCorrectamente() {
        MatchEntity entity = new MatchEntity();
        entity.setId("m1");
        entity.setDateTime(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertEquals("m1", entity.getId());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), entity.getDateTime());
    }

    @Test
    void estadoInicialEsPending() {
        MatchEntity entity = new MatchEntity();
        assertEquals("PENDING", entity.getStatus());
    }

    @Test
    void marcadorInicialEsCero() {
        MatchEntity entity = new MatchEntity();
        assertEquals(0, entity.getLocalScore());
        assertEquals(0, entity.getVisitorScore());
    }

    @Test
    void addCard_agregaTarjetaConReferencia() {
        MatchEntity match = new MatchEntity();
        match.setId("m1");

        CardEntity card = new CardEntity();
        card.setMinute(30);
        card.setType(CardEntity.CardType.YELLOW);

        match.addCard(card);

        assertEquals(1, match.getCards().size());
        assertSame(match, match.getCards().get(0).getMatch());
    }

    @Test
    void addGoal_agregaGolConReferencia() {
        MatchEntity match = new MatchEntity();
        match.setId("m1");

        GoalEntity goal = new GoalEntity();
        goal.setId("g1");
        goal.setMinute(15);

        match.addGoal(goal);

        assertEquals(1, match.getGoals().size());
        assertSame(match, match.getGoals().get(0).getMatch());
    }
}
