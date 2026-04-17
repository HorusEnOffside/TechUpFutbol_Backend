package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.TournamentResponseDTO;
import com.escuela.techcup.core.model.Tournament;

import java.util.Collections;
import java.util.List;

public class TournamentResponseMapper {

    private TournamentResponseMapper() {}

    public static TournamentResponseDTO toDTO(Tournament tournament) {
        if (tournament == null) return null;

        TournamentResponseDTO dto = new TournamentResponseDTO();
        dto.setId(tournament.getId());
        dto.setStartDate(tournament.getStartDate());
        dto.setEndDate(tournament.getEndDate());
        dto.setClosingDate(tournament.getClosingDate());
        dto.setTeamsMaxAmount(tournament.getTeamsMaxAmount());
        dto.setTeamCost(tournament.getTeamCost());
        dto.setStatus(tournament.getStatus());
        dto.setReglamento(tournament.getReglamento());
        dto.setSanciones(tournament.getSanciones());

        if (tournament.getCanchas() != null) {
            dto.setCanchas(tournament.getCanchas().stream().map(c -> {
                TournamentResponseDTO.CanchaResponseDTO canchaDTO = new TournamentResponseDTO.CanchaResponseDTO();
                canchaDTO.setId(c.getId());
                canchaDTO.setTipo(c.getTipo());
                canchaDTO.setNombre(c.getNombre());
                canchaDTO.setFotoUrl(c.getFotoUrl());
                return canchaDTO;
            }).toList());
        } else {
            dto.setCanchas(Collections.emptyList());
        }

        if (tournament.getHorarios() != null) {
            dto.setHorarios(tournament.getHorarios().stream().map(h -> {
                TournamentResponseDTO.HorarioResponseDTO horarioDTO = new TournamentResponseDTO.HorarioResponseDTO();
                horarioDTO.setId(h.getId());
                horarioDTO.setFecha(h.getFecha());
                horarioDTO.setDescripcion(h.getDescripcion());
                return horarioDTO;
            }).toList());
        } else {
            dto.setHorarios(Collections.emptyList());
        }

        return dto;
    }

    public static List<TournamentResponseDTO> toDTOList(List<Tournament> tournaments) {
        if (tournaments == null) return Collections.emptyList();
        return tournaments.stream().map(TournamentResponseMapper::toDTO).toList();
    }
}
