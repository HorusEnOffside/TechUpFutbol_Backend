package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import com.escuela.techcup.controller.dto.*;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.PlayerStatus;

public interface PlayerService {

    Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture);
    Player createSportsProfileTeacher(TeacherPlayerDTO teacherPlayerDTO, BufferedImage profilePicture);
    Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture);
    Player createSportsProfileGraduate(GraduatePlayerDTO graduatePlayerDTO, BufferedImage profilePicture);

    List<Player> getAllPlayers();
    Optional<Player> getPlayerByUserId(String userId);
    Optional<Player> findByNameContaining(String name);

    List<PlayerSearchResultDTO> searchPlayers(PlayerSearchDTO filters);

    Player updateStatus(String userId, PlayerStatus status);
}