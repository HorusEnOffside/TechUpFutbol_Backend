package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.core.model.Player;

public interface PlayerService {


    Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO);
    Player createSportsProfileStudent(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture);

    Player createSportsProfileTeacher(PlayerDTO playerDTO);
    Player createSportsProfileTeacher(PlayerDTO playerDTO, BufferedImage profilePicture);

    Player createSportsProfileFamiliar(PlayerDTO playerDTO);
    Player createSportsProfileFamiliar(PlayerDTO playerDTO, BufferedImage profilePicture);

    Player createSportsProfileGraduate(PlayerDTO playerDTO);
    Player createSportsProfileGraduate(PlayerDTO playerDTO, BufferedImage profilePicture);

}
