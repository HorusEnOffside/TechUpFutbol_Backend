package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.GraduatePlayerDTO;
import com.escuela.techcup.controller.dto.GraduateUserDTO;
import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.PlayerSearchDTO;
import com.escuela.techcup.controller.dto.PlayerSearchResultDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.TeacherPlayerDTO;
import com.escuela.techcup.controller.dto.TeacherUserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.validator.PlayerValidator;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.entity.users.UserEntity;
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

    public PlayerServiceImpl(PlayerRepository playerRepository,
                             UserPlayerRepository userPlayerRepository,
                             UserService userService) {
        this.playerRepository = playerRepository;
        this.userPlayerRepository = userPlayerRepository;
        this.userService = userService;
    }

    // ── Creación de perfiles deportivos ──────────────────────────────────

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

    // ── Consultas ────────────────────────────────────────────────────────

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

    // ── RF-15: Búsqueda de jugadores ─────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PlayerSearchResultDTO> searchPlayers(PlayerSearchDTO filters) {
        log.info("Searching players. filters={}", filters);

        // RN-15: Solo jugadores con status AVAILABLE
        List<PlayerEntity> available = playerRepository.findByStatus(PlayerStatus.AVAILABLE);

        return available.stream()
                .filter(p -> matchesFilters(p, filters))
                .map(this::toSearchResultDTO)
                .toList();
    }

    private boolean matchesFilters(PlayerEntity player, PlayerSearchDTO filters) {
        if (filters == null) return true;

        if (filters.getPosition() != null && player.getPosition() != filters.getPosition())
            return false;

        if (filters.getGender() != null && player.getUser().getGender() != filters.getGender())
            return false;

        if (filters.getName() != null && !filters.getName().isBlank()) {
            String filterName = filters.getName().trim().toLowerCase();
            String playerName = player.getUser().getName() != null
                    ? player.getUser().getName().toLowerCase() : "";
            if (!playerName.contains(filterName)) return false;
        }

        if (filters.getPlayerId() != null && !filters.getPlayerId().isBlank()) {
            if (!player.getId().equalsIgnoreCase(filters.getPlayerId().trim())) return false;
        }

        if (filters.getSemester() != null) {
            UserEntity user = player.getUser();
            if (!(user instanceof StudentEntity s)) return false;
            if (!filters.getSemester().equals(s.getSemester())) return false;
        }

        if (filters.getAge() != null) {
            LocalDate dob = player.getUser().getDateOfBirth();
            if (dob == null) return false;
            int age = LocalDate.now().getYear() - dob.getYear();
            if (age != filters.getAge()) return false;
        }

        return true;
    }

    private PlayerSearchResultDTO toSearchResultDTO(PlayerEntity player) {
        String teamName = player.getTeam() != null ? player.getTeam().getName() : null;
        return new PlayerSearchResultDTO(
                player.getId(),
                player.getUser().getName(),
                player.getUser().getMail(),
                player.getPosition(),
                player.getDorsalNumber(),
                player.getUser().getGender(),
                player.getStatus(),
                teamName
        );
    }

    // ── Helpers privados ─────────────────────────────────────────────────

    private Player savePlayerProfile(UserPlayer createdUser,
                                     com.escuela.techcup.core.model.enums.Position position,
                                     int dorsalNumber) {
        UserPlayerEntity userPlayerEntity = userPlayerRepository.findById(createdUser.getId())
                .orElseThrow(() -> new InvalidInputException("User player not found after creation"));

        if (playerRepository.existsByUserId(userPlayerEntity.getId()))
            throw new InvalidInputException("A sports profile already exists for that user");

        Player player = new Player(createdUser, position, dorsalNumber);
        PlayerEntity entity = PlayerMapper.toEntity(player);
        playerRepository.save(entity);
        return player;
    }

    @Override
    @Transactional
    public Player updateStatus(String userId, PlayerStatus status) {
        if (userId == null || userId.isBlank()) throw new InvalidInputException(USER_ID_IS_REQUIRED);
        if (status == null) throw new InvalidInputException("status is required");
        PlayerEntity entity = playerRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidInputException("Player not found for userId: " + userId));
        entity.setStatus(status);
        playerRepository.save(entity);
        return PlayerMapper.toModel(entity);
    }

    private void validatePlayerMailUnique(String mail) {
        if (mail == null || mail.isBlank()) throw new InvalidInputException("mail is required");
        if (userPlayerRepository.existsByMailIgnoreCase(mail))
            throw new InvalidInputException("A sports profile already exists for that email");
    }
}