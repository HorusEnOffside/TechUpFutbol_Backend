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
    private static final String PLAYER_DTO_VALIDATION_COMPLETED = "Player DTO validation completed for mail={}";
    private static final String PLAYER_DTO_IS_REQUIRED = "Player data is required";
    private static final String USER_ID_IS_REQUIRED = "userId is required";
    private static final String MAIL_IS_REQUIRED = "mail is required";

    private final List<Player> players = new ArrayList<>(); // temporary

    private final UserService userService;

    public PlayerServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture) {
        validateStudentPayload(studentPlayerDTO);
        log.debug("Starting player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition(), profilePicture != null);

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        validatePlayerMailUnique(studentPlayerDTO.getMail());
        log.trace(PLAYER_DTO_VALIDATION_COMPLETED, studentPlayerDTO.getMail());

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer playerUser = (profilePicture == null) 
            ? userService.createStudentUser(studentUserDTO)
            : userService.createStudentUser(studentUserDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Player sports profile created. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileTeacher(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting teacher player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace(PLAYER_DTO_VALIDATION_COMPLETED, playerDTO.getMail());

        UserPlayer playerUser = (profilePicture == null)
            ? userService.createTeacherUser(playerDTO)
            : userService.createTeacherUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Teacher player sports profile created. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting familiar player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace(PLAYER_DTO_VALIDATION_COMPLETED, playerDTO.getMail());

        UserPlayer playerUser = (profilePicture == null)
            ? userService.createFamiliarUser(playerDTO)
            : userService.createFamiliarUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Familiar player sports profile created. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public Player createSportsProfileGraduate(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting graduate player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());
        validatePlayerMailUnique(playerDTO.getMail());
        log.trace(PLAYER_DTO_VALIDATION_COMPLETED, playerDTO.getMail());

        UserPlayer playerUser = (profilePicture == null)
            ? userService.createGraduateUser(playerDTO)
            : userService.createGraduateUser(playerDTO, profilePicture);
        Player createdPlayer = new Player(playerUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
        players.add(createdPlayer);

        log.info("Graduate player sports profile created. userId={}", createdPlayer.getUserId());
        return createdPlayer;
    }

    @Override
    public List<Player> getAllPlayers() {
        return new ArrayList<>(players);
    }

    @Override
    public Optional<Player> getPlayerByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            log.warn("Cannot search player by empty userId");
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }

        return players.stream()
            .filter(player -> player.getUserId().equals(userId))
            .findFirst();
    }

    private void validatePlayerMailUnique(String mail) {
        if (mail == null || mail.isBlank()) {
            log.warn("Cannot validate player uniqueness with empty mail");
            throw new InvalidInputException(MAIL_IS_REQUIRED);
        }

        boolean exists = players.stream()
            .anyMatch(player -> player.getMail().equalsIgnoreCase(mail));
        if (exists) {
            log.warn("Sports profile already exists for mail={}", mail);
            throw new InvalidInputException("A sports profile already exists for that email");
        }
    }

    private void validateStudentPayload(StudentPlayerDTO dto) {
        if (dto == null) {
            log.warn("Student sports profile creation rejected: payload is null");
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }
    }

    private void validatePlayerPayload(PlayerDTO dto) {
        if (dto == null) {
            log.warn("Player sports profile creation rejected: payload is null");
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }
    }


}
