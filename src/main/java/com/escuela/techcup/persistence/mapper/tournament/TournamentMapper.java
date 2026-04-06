package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

public class TournamentMapper {

    private TournamentMapper() {}

    public static Tournament toModel(TournamentEntity entity) {
        if (entity == null) return null;
        Tournament tournament = new Tournament();
        tournament.setId(entity.getId());
        tournament.setStartDate(entity.getStartDate());
        tournament.setEndDate(entity.getEndDate());
        tournament.setClosingDate(entity.getClosingDate());
        tournament.setTeamsMaxAmount(entity.getTeamsMaxAmount());
        tournament.setTeamCost(entity.getTeamCost());
        tournament.setStatus(entity.getStatus());
        tournament.setReglamento(entity.getReglamento());
        tournament.setCanchas(entity.getCanchas());
        tournament.setHorarios(entity.getHorarios());
        tournament.setSanciones(entity.getSanciones());
        return tournament;
    }

    public static TournamentEntity toEntity(Tournament model) {
        if (model == null) return null;
        TournamentEntity entity = new TournamentEntity();
        entity.setId(model.getId());
        entity.setStartDate(model.getStartDate());
        entity.setEndDate(model.getEndDate());
        entity.setClosingDate(model.getClosingDate());
        entity.setTeamsMaxAmount(model.getTeamsMaxAmount());
        entity.setTeamCost(model.getTeamCost());
        entity.setStatus(model.getStatus());
        entity.setReglamento(model.getReglamento());
        entity.setCanchas(model.getCanchas());
        entity.setHorarios(model.getHorarios());
        entity.setSanciones(model.getSanciones());
        return entity;
    }
}