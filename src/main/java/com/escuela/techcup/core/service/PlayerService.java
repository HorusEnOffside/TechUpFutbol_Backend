package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.core.model.Player;

public interface PlayerService {


    Player createSportsProfile(StudentPlayerDTO studentPlayerDTO);
    Player createSportsProfile(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture);

    //Student createSportsProfile(StudentPlayerDTO studentPlayerDTO);
    //Student createSportsProfile(StudentPlayerDTO studentPlayerDTO, BufferedImage profilePicture);
}
