package com.escuela.techcup.core.service.impl;

import com.escuela.techcup.controller.dto.TeamDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Team;
import com.escuela.techcup.core.service.TeamService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.core.validator.TeamValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceImpl.class);
    private static final String TEAM_DTO_IS_REQUIRED = "Team data is required";

    @Override
    public Team createTeam(TeamDTO teamDTO, BufferedImage logo){
        log.debug("Starting team creation. name={}", teamDTO.getName());
        verifyTeam(teamDTO);
        verifyColors(teamDTO.getUniformColors());

        Team team = new Team();
        team.setId(idGenerator());
        team.setName(teamDTO.getName());
        team.setUniformColors(teamDTO.getUniformColors());
        team.setPlayers(teamDTO.getPlayers());
        team.setFormation(teamDTO.getFormation());

        if (logo != null) {
            team.setLogo(logo);
        }

        log.info("Team created. teamId={}, name={}", team.getId(), team.getName());

        //IMPLEMENTAR REPOSITOY ---- teamRepository.save(team)
        return team;
    }

    private void verifyTeam(TeamDTO teamDTO) {
        if (teamDTO == null) {
            log.warn("Team creation rejected: payload is null");
            throw new InvalidInputException(TEAM_DTO_IS_REQUIRED);
        }

        log.trace("Validating team input. name={}", teamDTO.getName());
        TeamValidator.validateInput(teamDTO.getName(), teamDTO.getUniformColors(), teamDTO.getFormation());

        //IMPLEMENTACION DE PERSISTENCIA
        //if (teamRepository.existsByName(teamDTO.getName())) {
        //    log.warn("Team already exists for name={}", teamDTO.getName());
        //    throw new InvalidInputException("A team is already registered with that name");
        //}
        log.trace("Team input validation completed. name={}", teamDTO.getName());
    }

    //RELACIONADO CON PERSISTENCIA, FALTA POR HACER
    public void verifyColors(List<Color> colors){}

    //FALTA
    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }

}
