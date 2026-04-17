package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Card;
import com.escuela.techcup.core.model.ComponentPlayer;
import com.escuela.techcup.persistence.entity.tournament.CardEntity;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;

public class CardMapper {
    public static Card toModel(CardEntity entity) {
        if (entity == null) return null;
        ComponentPlayer player = PlayerMapper.toModel(entity.getPlayer());
        return new Card(
                entity.getId() != null ? entity.getId().toString() : null,
                entity.getMinute(),
                player,
                Card.CardType.valueOf(entity.getType().name()),
                entity.getDescription()
        );
    }

    public static CardEntity toEntity(Card card) {
        if (card == null) return null;
        CardEntity entity = new CardEntity();
        entity.setMinute(card.getMinute());
        entity.setType(CardEntity.CardType.valueOf(card.getType().name()));
        entity.setDescription(card.getDescription());
        if (card.getPlayer() != null) {
            entity.setPlayer(PlayerMapper.toEntity(card.getPlayer()));
        }
        return entity;
    }
}

