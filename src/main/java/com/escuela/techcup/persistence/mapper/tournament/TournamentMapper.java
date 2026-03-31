package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

public class TournamentMapper {
    private TournamentMapper() {}

    public static TournamentEntity toEntity(Tournament tournament) {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(tournament.getId());
        entity.setStartDate(tournament.getStartDate());
        entity.setEndDate(tournament.getEndDate());
        entity.setTeamsMaxAmount(tournament.getTeamsMaxAmount());
        entity.setTeamCost(tournament.getTeamCost());
        entity.setStatus(tournament.getStatus());
        return entity;
    }

    public static Tournament toModel(TournamentEntity entity) {
        Tournament model = new Tournament();
        model.setId(entity.getId());
        model.setStartDate(entity.getStartDate());
        model.setEndDate(entity.getEndDate());
        model.setTeamsMaxAmount(entity.getTeamsMaxAmount());
        model.setTeamCost(entity.getTeamCost());
        model.setStatus(entity.getStatus());
        return model;
    }
}
