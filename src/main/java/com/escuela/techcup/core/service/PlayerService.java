package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.core.model.Player;

public interface PlayerService {

	Player createSportsProfile(PlayerDTO playerDTO, BufferedImage profilePicture);

    Player createSportsProfile(PlayerDTO playerDTO);


}
