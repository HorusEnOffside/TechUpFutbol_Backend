package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Service;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.controller.mapper.PlayerMapper;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.core.util.PasswordHashUtil;
import com.escuela.techcup.core.validator.PlayerValidator;

@Service
public class PlayerServiceImpl implements PlayerService {
    //cuando se implemente persistencia verificar cosas del negocio, como que no se repitan emails, o que el dorsal number no se repita en el mismo equipo, etc.

    private final UserServiceImpl userService;

    public PlayerServiceImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public Player createSportsProfile(StudentPlayerDTO studentPlayerDTO) {
        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUSer = userService.createStudentUser(studentUserDTO);
        return new Player(playerUSer, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
    }

    @Override
    public Player createSportsProfile(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture) {
        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUSer = userService.createStudentUser(studentUserDTO, profilePicture);
        return new Player(playerUSer, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
    }


}
