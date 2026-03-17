package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.validator.PlayerValidator;

@Service
public class PlayerServiceImpl implements PlayerService {
    //cuando se implemente persistencia verificar cosas del negocio, como que no se repitan emails, o que el dorsal number no se repita en el mismo equipo, etc.

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final UserServiceImpl userService;

    public PlayerServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public Player createSportsProfile(StudentPlayerDTO studentPlayerDTO) {
        log.debug("Starting player sports profile creation without photo. mail={}, dorsal={}, position={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition());

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        log.trace("Player DTO validation completed for mail={}", studentPlayerDTO.getMail());

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUSer = userService.createStudentUser(studentUserDTO);
        Player createdPlayer = new Player(playerUSer, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());

        log.info("Player sports profile created in service. userId={}, mail={}", createdPlayer.getUserId(), createdPlayer.getMail());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfile(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting player sports profile creation with photo. mail={}, dorsal={}, position={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition());

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        log.trace("Player DTO validation completed for mail={}", studentPlayerDTO.getMail());

        if (profilePicture == null) {
            log.warn("Profile picture is null for mail={}", studentPlayerDTO.getMail());
        }

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUSer = userService.createStudentUser(studentUserDTO, profilePicture);
        Player createdPlayer = new Player(playerUSer, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());

        log.info("Player sports profile with photo created in service. userId={}, mail={}", createdPlayer.getUserId(), createdPlayer.getMail());
        return createdPlayer;
    }


}
