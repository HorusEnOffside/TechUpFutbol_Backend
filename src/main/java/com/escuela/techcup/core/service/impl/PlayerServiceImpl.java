package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.core.util.PasswordHashUtil;
import com.escuela.techcup.core.validator.PlayerValidator;

@Service
public class PlayerServiceImpl implements PlayerService {


    @Override
    public Player createSportsProfile(PlayerDTO playerDTO) {
        String id = IdGeneratorUtil.generateId();
        String password = PasswordHashUtil.hashPassword(playerDTO.getPassword());
        
        PlayerValidator.validateInput(id, playerDTO.getName(), playerDTO.getEmail(), playerDTO.getDorsalNumber());

        return PlayerMapper.toPlayer(playerDTO, id, password);
    }

    @Override
    public Player createSportsProfile(PlayerDTO playerDTO, BufferedImage profilePicture) {
        String id = IdGeneratorUtil.generateId();
        String password = PasswordHashUtil.hashPassword(playerDTO.getPassword());

        PlayerValidator.validateInput(id, playerDTO.getName(), playerDTO.getEmail(), playerDTO.getDorsalNumber());

        return PlayerMapper.toPlayer(playerDTO, id, profilePicture, password);
    }

}
