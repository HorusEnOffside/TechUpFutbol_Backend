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
    //cuando se implemente persistencia verificar cosas del negocio, como que no se repitan emails, o que el dorsal number no se repita en el mismo equipo, etc.

    @Override
    public Player createSportsProfile(PlayerDTO playerDTO) {
        String id = idGenerator();
        String password = hashPassword(playerDTO.getPassword());
        
        PlayerValidator.validateInput(id, playerDTO.getName(), playerDTO.getEmail(), playerDTO.getDorsalNumber());

        return PlayerMapper.toPlayer(playerDTO, id, password);
    }

    @Override
    public Player createSportsProfile(PlayerDTO playerDTO, BufferedImage profilePicture) {
        String id = idGenerator();
        String password = hashPassword(playerDTO.getPassword());

        PlayerValidator.validateInput(id, playerDTO.getName(), playerDTO.getEmail(), playerDTO.getDorsalNumber());

        return PlayerMapper.toPlayer(playerDTO, id, profilePicture, password);
    }


    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
    private String hashPassword(String password) {
        return PasswordHashUtil.hashPassword(password);
    }

}
