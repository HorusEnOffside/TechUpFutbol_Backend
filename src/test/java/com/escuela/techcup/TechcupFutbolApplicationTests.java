package com.escuela.techcup;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.escuela.techcup.controller.dto.StudentPlayerDTO;
import com.escuela.techcup.core.exception.ValidationException;
import com.escuela.techcup.core.model.Player;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.service.PlayerService;
import com.escuela.techcup.core.util.PasswordHashUtil;

@SpringBootTest
class TechcupFutbolApplicationTests {
	@Autowired
	private PlayerService playerService;

	@Test
	void contextLoads() {
		// Este test simplemente verifica que el contexto de Spring se carga correctamente.
	}

	@Test
	void testCreationPlayer() {
		StudentPlayerDTO playerDTO = buildValidPlayerDTO();

		Player player = playerService.createSportsProfileStudent(playerDTO, null);

		assertNotNull(player);
		assertNotNull(player.getUserPlayer());
		assertNotNull(player.getUserPlayer().getId());
		assertEquals(playerDTO.getName(), player.getUserPlayer().getName());
		assertEquals(playerDTO.getMail(), player.getUserPlayer().getMail());
		assertEquals(playerDTO.getDateOfBirth(), player.getUserPlayer().getDateOfBirth());
		assertEquals(playerDTO.getGender(), player.getUserPlayer().getGender());
		assertEquals(10, player.getDorsalNumber());
		assertEquals(playerDTO.getPosition(), player.getPosition());
		assertEquals(PlayerStatus.AVAILABLE, player.getStatus());
		assertNotEquals(playerDTO.getPassword(), player.getUserPlayer().getPassword());
		assertTrue(PasswordHashUtil.verifyPassword(playerDTO.getPassword(), player.getUserPlayer().getPassword()));
	}

	@Test
	void testCreationPlayerWithInvalidEmailShouldThrowException() {
		StudentPlayerDTO playerDTO = buildValidPlayerDTO();
		playerDTO.setMail("correo-invalido");

		assertThrows(ValidationException.class, () -> playerService.createSportsProfileStudent(playerDTO, null));
	}

	private StudentPlayerDTO buildValidPlayerDTO() {
		StudentPlayerDTO dto = new StudentPlayerDTO();
		dto.setName("Andres");
		dto.setMail("andres@techcup.com");
		dto.setDateOfBirth(LocalDate.of(2000, 1, 15));
		dto.setGender(Gender.MALE);
		dto.setPassword("A123456789");
		dto.setSemester(4);
		dto.setDorsalNumber(10);
		dto.setPosition(Position.MIDFIELDER);
		return dto;
	}
}