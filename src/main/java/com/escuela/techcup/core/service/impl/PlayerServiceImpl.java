package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.validator.PlayerValidator;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.mapper.users.PlayerMapper;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.UserPlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);
    private static final String PLAYER_DTO_IS_REQUIRED = "Player data is required";
    private static final String USER_ID_IS_REQUIRED = "userId is required";

    private final PlayerRepository playerRepository;
    private final UserPlayerRepository userPlayerRepository;
    private final UserService userService;

    public PlayerServiceImpl(PlayerRepository playerRepository, UserPlayerRepository userPlayerRepository, UserService userService) {
        this.playerRepository = playerRepository;
        this.userPlayerRepository = userPlayerRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture) {
        validateStudent(studentPlayerDTO);
        log.debug("Starting player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            studentPlayerDTO.getMail(), studentPlayerDTO.getDorsalNumber(), studentPlayerDTO.getPosition(), profilePicture != null);

        validatePlayerMailUnique(studentPlayerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", studentPlayerDTO.getMail());

        StudentUserDTO studentUserDTO = new StudentUserDTO(studentPlayerDTO.getName(), studentPlayerDTO.getMail(), studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(), studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester());

        UserPlayer createdUser = userService.createStudentUser(studentUserDTO, profilePicture);


        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }

        Player player = new Player(createdUser, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());

        PlayerEntity entity = PlayerMapper.toEntity(player);

        playerRepository.save(entity);
        return player;
    }



    @Override
    @Transactional
    public Player createSportsProfileTeacher(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting teacher player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());


        UserPlayer createdUser = createUserPlayer(playerDTO, profilePicture);


        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }
        log.debug("Creating player entity for userId={}", createdUser.getId());
        Player player = new Player(createdUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());

        PlayerEntity entity = PlayerMapper.toEntity(player);

        playerRepository.save(entity);
        return player;
    }

    @Override
    @Transactional
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting familiar player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer createdUser = createUserPlayer(playerDTO, profilePicture);


        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }
        log.debug("Creating player entity for userId={}", createdUser.getId());
        Player player = new Player(createdUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());

        PlayerEntity entity = PlayerMapper.toEntity(player);

        playerRepository.save(entity);
        return player;
    }

    @Override
    @Transactional
    public Player createSportsProfileGraduate(PlayerDTO playerDTO, BufferedImage profilePicture) {
        validatePlayerPayload(playerDTO);
        log.debug("Starting graduate player sports profile creation. mail={}, dorsal={}, position={}, photo={}",
            playerDTO.getMail(), playerDTO.getDorsalNumber(), playerDTO.getPosition(), profilePicture != null);

        validatePlayerMailUnique(playerDTO.getMail());
        log.trace("Player DTO validation completed for mail={}", playerDTO.getMail());

        UserPlayer createdUser = createUserPlayer(playerDTO, profilePicture);

        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }
        log.debug("Creating player entity for userId={}", createdUser.getId());
        Player player = new Player(createdUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());

        PlayerEntity entity = PlayerMapper.toEntity(player);

        playerRepository.save(entity);
        return player;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(PlayerMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }
        return playerRepository.findByUserId(userId)
                .map(PlayerMapper::toModel);
    }



    private UserPlayer createUserPlayer(PlayerDTO playerDTO, BufferedImage profilePicture) {
        UserPlayerDTO userPlayerDTO = new UserPlayerDTO(playerDTO.getName(), playerDTO.getMail(), playerDTO.getDateOfBirth(), playerDTO.getGender(), playerDTO.getPassword());
        return userService.createTeacherUser(userPlayerDTO, profilePicture);
    }

    private void validatePlayerMailUnique(String mail) {
        if (mail == null || mail.isBlank()) {
            log.warn("Cannot validate player uniqueness with empty mail");
            throw new InvalidInputException("mail is required");
        }

        boolean exists = userPlayerRepository.existsByMailIgnoreCase(mail);
        if (exists) {
            log.warn("Sports profile already exists for mail={}", mail);
            throw new InvalidInputException("A sports profile already exists for that email");
        }
    }

    private void validateStudent(StudentPlayerDTO dto) {
        if (dto == null) {
            log.warn("Student sports profile creation rejected: payload is null");
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }

        PlayerValidator.validateInput(dto.getDorsalNumber());
    }

    private void validatePlayerPayload(PlayerDTO dto) {
        if (dto == null) {
            log.warn("Player sports profile creation rejected: payload is null");
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }
    }
}