package com.escuela.techcup.persistence.mapper.tournament;

import com.escuela.techcup.core.model.Cancha;
import com.escuela.techcup.core.model.Horario;
import com.escuela.techcup.core.model.Tournament;
import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

import java.util.List;

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
        tournament.setSanciones(entity.getSanciones());

        List<Cancha> canchas = entity.getCanchas().stream().map(c -> {
            Cancha cancha = new Cancha();
            cancha.setId(c.getId());
            cancha.setNombre(c.getNombre());
            cancha.setFoto(c.getFoto());
            return cancha;
        }).toList();
        tournament.setCanchas(canchas);

        List<Horario> horarios = entity.getHorarios().stream().map(h -> {
            Horario horario = new Horario();
            horario.setId(h.getId());
            horario.setFecha(h.getFecha());
            horario.setDescripcion(h.getDescripcion());
            return horario;
        }).toList();
        tournament.setHorarios(horarios);

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
        entity.setSanciones(model.getSanciones());
        return entity;
    }
}
