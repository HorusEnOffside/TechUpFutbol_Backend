package com.escuela.techcup.service;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;

import com.escuela.techcup.model.Player;
import com.escuela.techcup.model.enums.Gender;
import com.escuela.techcup.model.enums.PlayerType;
import com.escuela.techcup.model.enums.Position;

public interface PlayerService {

	Player createSportsProfile(
		String id,
		String name,
		String email,
		LocalDate dateOfBirth,
		Gender gender,
		PlayerType playerType,
		int dorsalNumber,
        Position position,
		BufferedImage profilePicture,
        String password
	);

    Player createSportsProfile(
		String id,
		String name,
		String email,
		LocalDate dateOfBirth,
		Gender gender,
		PlayerType playerType,
		int dorsalNumber,
        Position position,
        String password
	);


}
