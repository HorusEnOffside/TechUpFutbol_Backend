package com.escuela.techcup;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.escuela.techcup.controller.dto.PlayerDTO;
import com.escuela.techcup.core.exception.TechcupException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.PlayerType;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.util.PasswordHashUtil;

@SpringBootTest
class TechcupFutbolApplicationTests {
	@Autowired
	private PlayerService playerService;

	@Test
	void contextLoads() {
	}

	@Test
	void testCreationPlayer() {
		PlayerDTO playerDTO = buildValidPlayerDTO();

		Player player = playerService.createSportsProfile(playerDTO);

		assertNotNull(player);
		assertNotNull(player.getUserPlayer());
		assertNotNull(player.getUserPlayer().getId());
		assertEquals(playerDTO.getName(), player.getUserPlayer().getName());
		assertEquals(playerDTO.getEmail(), player.getUserPlayer().getEmail());
		assertEquals(playerDTO.getDateOfBirth(), player.getUserPlayer().getDateOfBirth());
		assertEquals(playerDTO.getGender(), player.getUserPlayer().getGender());
		assertEquals(playerDTO.getPlayerType(), player.getUserPlayer().getPlayerType());
		assertEquals(playerDTO.getDorsalNumber(), player.getDorsalNumber());
		assertEquals(playerDTO.getPosition(), player.getPosition());
		assertEquals(PlayerStatus.DISPONIBLE, player.getStatus());
		assertNotEquals(playerDTO.getPassword(), player.getUserPlayer().getPassword());
		assertTrue(PasswordHashUtil.verifyPassword(playerDTO.getPassword(), player.getUserPlayer().getPassword()));
	}

	@Test
	void testCreationPlayerWithInvalidEmailShouldThrowException() {
		PlayerDTO playerDTO = buildValidPlayerDTO();
		playerDTO.setEmail("correo-invalido");

		assertThrows(TechcupException.InvalidInputException.class, () -> playerService.createSportsProfile(playerDTO));
	}

	private PlayerDTO buildValidPlayerDTO() {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setName("Andres");
		playerDTO.setEmail("andres@techcup.com");
		playerDTO.setDateOfBirth(LocalDate.of(2000, 1, 15));
		playerDTO.setGender(Gender.HOMBRE);
		playerDTO.setPlayerType(PlayerType.ESTUDIANTE);
		playerDTO.setDorsalNumber(10);
		playerDTO.setPosition(Position.VOLANTE);
		playerDTO.setPassword("1234");
		return playerDTO;
	}
}