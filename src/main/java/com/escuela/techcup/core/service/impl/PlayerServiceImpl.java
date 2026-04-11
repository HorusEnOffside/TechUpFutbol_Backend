package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.GraduatePlayerDTO;
import com.escuela.techcup.controller.dto.GraduateUserDTO;
import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.TeacherPlayerDTO;
import com.escuela.techcup.controller.dto.TeacherUserDTO;
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
        if (studentPlayerDTO == null) throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        PlayerValidator.validateInput(studentPlayerDTO.getDorsalNumber());
        validatePlayerMailUnique(studentPlayerDTO.getMail());

        log.debug("Starting student player sports profile creation. mail={}", studentPlayerDTO.getMail());

        StudentUserDTO studentUserDTO = new StudentUserDTO(
                studentPlayerDTO.getName(), studentPlayerDTO.getMail(),
                studentPlayerDTO.getDateOfBirth(), studentPlayerDTO.getGender(),
                studentPlayerDTO.getPassword(), studentPlayerDTO.getSemester(),
                studentPlayerDTO.getCareer());

        UserPlayer createdUser = userService.createStudentUser(studentUserDTO, profilePicture);
        return savePlayerProfile(createdUser, studentPlayerDTO.getPosition(), studentPlayerDTO.getDorsalNumber());
    }

    @Override
    @Transactional
    public Player createSportsProfileTeacher(TeacherPlayerDTO teacherPlayerDTO, BufferedImage profilePicture) {
        if (teacherPlayerDTO == null) throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        validatePlayerMailUnique(teacherPlayerDTO.getMail());

        log.debug("Starting teacher player sports profile creation. mail={}", teacherPlayerDTO.getMail());

        TeacherUserDTO teacherUserDTO = new TeacherUserDTO(
                teacherPlayerDTO.getName(), teacherPlayerDTO.getMail(),
                teacherPlayerDTO.getDateOfBirth(), teacherPlayerDTO.getGender(),
                teacherPlayerDTO.getPassword(), teacherPlayerDTO.getCareer());

        UserPlayer createdUser = userService.createTeacherUser(teacherUserDTO, profilePicture);
        return savePlayerProfile(createdUser, teacherPlayerDTO.getPosition(), teacherPlayerDTO.getDorsalNumber());
    }

    @Override
    @Transactional
    public Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture) {
        if (playerDTO == null) throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        validatePlayerMailUnique(playerDTO.getMail());

        log.debug("Starting familiar player sports profile creation. mail={}", playerDTO.getMail());

        UserPlayerDTO userPlayerDTO = new UserPlayerDTO(
                playerDTO.getName(), playerDTO.getMail(),
                playerDTO.getDateOfBirth(), playerDTO.getGender(),
                playerDTO.getPassword());

        UserPlayer createdUser = userService.createFamiliarUser(userPlayerDTO, profilePicture);
        return savePlayerProfile(createdUser, playerDTO.getPosition(), playerDTO.getDorsalNumber());
    }

    @Override
    @Transactional
    public Player createSportsProfileGraduate(GraduatePlayerDTO graduatePlayerDTO, BufferedImage profilePicture) {
        if (graduatePlayerDTO == null) throw new InvalidInputException(PLAYER_DTO_IS_REQUIRED);
        validatePlayerMailUnique(graduatePlayerDTO.getMail());

        log.debug("Starting graduate player sports profile creation. mail={}", graduatePlayerDTO.getMail());

        GraduateUserDTO graduateUserDTO = new GraduateUserDTO(
                graduatePlayerDTO.getName(), graduatePlayerDTO.getMail(),
                graduatePlayerDTO.getDateOfBirth(), graduatePlayerDTO.getGender(),
                graduatePlayerDTO.getPassword(), graduatePlayerDTO.getCareer());

        UserPlayer createdUser = userService.createGraduateUser(graduateUserDTO, profilePicture);
        return savePlayerProfile(createdUser, graduatePlayerDTO.getPosition(), graduatePlayerDTO.getDorsalNumber());
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
        if (userId == null || userId.isBlank()) throw new InvalidInputException(USER_ID_IS_REQUIRED);
        return playerRepository.findByUserId(userId).map(PlayerMapper::toModel);
    }

    private Player savePlayerProfile(UserPlayer createdUser, com.escuela.techcup.core.model.enums.Position position, int dorsalNumber) {
        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId())) {
            throw new InvalidInputException("A sports profile already exists for that user");
        }


        Player player = new Player(createdUser, position, dorsalNumber);
        PlayerEntity entity = PlayerMapper.toEntity(player);
        playerRepository.save(entity);
        return player;
    }

    private void validatePlayerMailUnique(String mail) {
        if (mail == null || mail.isBlank()) throw new InvalidInputException("mail is required");
        if (userPlayerRepository.existsByMailIgnoreCase(mail)) {
            throw new InvalidInputException("A sports profile already exists for that email");
        }
    }
}