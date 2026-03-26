package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.escuela.techcup.core.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.validator.PlayerValidator;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;
import com.escuela.techcup.persistence.repository.PlayerRepository;
import com.escuela.techcup.persistence.repository.UserPlayerRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

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
        if (studentPlayerDTO == null) {
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }

        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());

        StudentUserDTO studentUserDTO = new StudentUserDTO(
                studentPlayerDTO.getName(),
                studentPlayerDTO.getMail(),
                studentPlayerDTO.getDateOfBirth(),
                studentPlayerDTO.getGender(),
                studentPlayerDTO.getPassword(),
                studentPlayerDTO.getSemester()
        );

        UserPlayer createdUser = (profilePicture == null)
                ? userService.createStudentUser(studentUserDTO)
                : userService.createStudentUser(studentUserDTO, profilePicture);

        UUID userId = UUID.fromString(createdUser.getId());

        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }

        PlayerEntity entity = new PlayerEntity();
        entity.setUser(userPlayerEntity);
        entity.setDorsalNumber(studentPlayerDTO.getDorsalNumber());
        entity.setPosition(studentPlayerDTO.getPosition());
        entity.setStatus(PlayerStatus.AVAILABLE);

        PlayerEntity saved = playerRepository.save(entity);
        return toCorePlayer(saved);
    }

    @Override
    @Transactional
    public Player createSportsProfileTeacher(PlayerDTO playerDTO, BufferedImage profilePicture) {
        return createSportsProfileFromUserPlayer(userService::createTeacherUser, userService::createTeacherUser, playerDTO, profilePicture);
    }

    @Override
    @Transactional
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture) {
        return createSportsProfileFromUserPlayer(userService::createFamiliarUser, userService::createFamiliarUser, playerDTO, profilePicture);
    }

    @Override
    @Transactional
    public Player createSportsProfileGraduate(PlayerDTO playerDTO, BufferedImage profilePicture) {
        return createSportsProfileFromUserPlayer(userService::createGraduateUser, userService::createGraduateUser, playerDTO, profilePicture);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Player> getAllPlayers() {
        return playerRepository.findAll().stream().map(this::toCorePlayer).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> getPlayerByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }
        return playerRepository.findByUserId(userId).map(this::toCorePlayer);
    }

    private Player createSportsProfileFromUserPlayer(
            Function<PlayerDTO, UserPlayer> creatorNoPhoto,
            BiFunction<PlayerDTO, BufferedImage, UserPlayer> creatorWithPhoto,
            PlayerDTO playerDTO,
            BufferedImage profilePicture
    ) {
        if (playerDTO == null) {
            throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        }

        PlayerValidator.validateInput(playerDTO.getDorsalNumber());

        UserPlayer createdUser = (profilePicture == null)
                ? creatorNoPhoto.apply(playerDTO)
                : creatorWithPhoto.apply(playerDTO, profilePicture);

        UUID userId = UUID.fromString(createdUser.getId());

        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(userId)
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }

        PlayerEntity entity = new PlayerEntity();
        entity.setUser(userPlayerEntity);
        entity.setDorsalNumber(playerDTO.getDorsalNumber());
        entity.setPosition(playerDTO.getPosition());
        entity.setStatus(PlayerStatus.AVAILABLE);

        PlayerEntity saved = playerRepository.save(entity);
        return toCorePlayer(saved);
    }

    private Player toCorePlayer(PlayerEntity e) {
        User user = userService.getUserById(e.getUser().getId().toString())
                .orElseThrow(() -> new InvalidInputException("User not found for player"));

        if (!(user instanceof UserPlayer userPlayer)) {
            throw new InvalidInputException("User is not a UserPlayer");
        }

        Player player = new Player(userPlayer, e.getPosition(), e.getDorsalNumber());
        player.setStatus(e.getStatus());
        return player;
    }
}