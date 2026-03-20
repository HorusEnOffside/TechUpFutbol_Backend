package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.validator.PlayerValidator;

@Service
public class PlayerServiceImpl implements PlayerService {
    //cuando se implemente persistencia verificar cosas del negocio, como que no se repitan emails, o que el dorsal number no se repita en el mismo equipo, etc.

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final List<Player> players = new ArrayList<>(); //temporal

    private final UserService userService;

    public PlayerServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO) {
        log.debug("Starting player sports profile creation without photo. mail={}, dorsal={}, position={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition());

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        validatePlayerMailUnique(studentPlayerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", studentPlayerDTO.getMail());

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUser = userService.createStudentUser(studentUserDTO);
        Player createdPlayer = new Player(playerUser, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Player sports profile created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting player sports profile creation with photo. mail={}, dorsal={}, position={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition());

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        validatePlayerMailUnique(studentPlayerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", studentPlayerDTO.getMail());

        if (profilePicture == null) {
            log.warn("Profile picture is null for mail={}", studentPlayerDTO.getMail());
            return createSportsProfileStudent(studentPlayerDTO);
        }

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUser = userService.createStudentUser(studentUserDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Player sports profile with photo created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileTeacher(PlayerDTO playerDTO) {
        log.debug("Starting teacher player sports profile creation without photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createTeacherUser(playerDTO);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Teacher player sports profile created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileTeacher(PlayerDTO playerDTO, BufferedImage profilePicture) {
        log.debug("Starting teacher player sports profile creation with photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        if (profilePicture == null) {
            log.warn("Profile picture is null for mail={}", playerDTO.getMail());
            return createSportsProfileTeacher(playerDTO);
        }

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createTeacherUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Teacher player sports profile with photo created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO) {
        log.debug("Starting familiar player sports profile creation without photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createFamiliarUser(playerDTO);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Familiar player sports profile created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture) {
        log.debug("Starting familiar player sports profile creation with photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        if (profilePicture == null) {
            log.warn("Profile picture is null for mail={}", playerDTO.getMail());
            return createSportsProfileFamiliar(playerDTO);
        }

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createFamiliarUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Familiar player sports profile with photo created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileGraduate(PlayerDTO playerDTO) {
        log.debug("Starting graduate player sports profile creation without photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createGraduateUser(playerDTO);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Graduate player sports profile created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileGraduate(PlayerDTO playerDTO, BufferedImage profilePicture) {
        log.debug("Starting graduate player sports profile creation with photo. mail={}, dorsal={}, position={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition());

        if (profilePicture == null) {
            log.warn("Profile picture is null for mail={}", playerDTO.getMail());
            return createSportsProfileGraduate(playerDTO);
        }

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer playerUser = userService.createGraduateUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Graduate player sports profile with photo created in service. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Optional<Player> getPlayerByUserId(String userId) {
        return players.stream()
            .filter(player -> player.getUserId().equals(userId))
            .findFirst();
    }

    private void validatePlayerMailUnique(String mail) {
        boolean exists = players.stream()
            .anyMatch(player -> player.getMail().equalsIgnoreCase(mail));
        if (exists) {
            throw new InvalidInputException("Ya existe un perfil deportivo para ese correo");
        }
    }


}
