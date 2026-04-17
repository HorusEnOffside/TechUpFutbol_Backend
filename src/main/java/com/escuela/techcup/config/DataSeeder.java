package com.escuela.techcup.config;

import com.escuela.techcup.core.model.enums.Career;
import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.util.PasswordHashUtil;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import com.escuela.techcup.persistence.entity.users.StudentEntity;
import com.escuela.techcup.persistence.repository.users.PlayerRepository;
import com.escuela.techcup.persistence.repository.users.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@Order(1)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final StudentRepository studentRepository;
    private final PlayerRepository playerRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (playerRepository.count() >= 7) {
            log.info("DataSeeder: players already exist, skipping seed.");
            return;
        }

        String passwordHash = PasswordHashUtil.hashPassword("pass1234");

        List<Object[]> seedData = List.of(
            new Object[]{"Carlos Andres Gomez",  "carlos.gomez@test.com",   LocalDate.of(2001, 3, 15),  Gender.MALE,   Career.INGENIERIA_DE_SISTEMAS,     3, Position.GOALKEEPER},
            new Object[]{"Laura Sofia Reyes",    "laura.reyes@test.com",    LocalDate.of(2002, 7, 22),  Gender.FEMALE, Career.INGENIERIA_DE_SISTEMAS,     5, Position.GOALKEEPER},
            new Object[]{"Miguel Angel Torres",  "miguel.torres@test.com",  LocalDate.of(2000, 11, 8),  Gender.MALE,   Career.INTELIGENCIA_ARTIFICIAL,    5, Position.DEFENDER},
            new Object[]{"Valentina Cruz",       "valentina.cruz@test.com", LocalDate.of(2003, 1, 30),  Gender.FEMALE, Career.INGENIERIA_DE_SISTEMAS,     3, Position.DEFENDER},
            new Object[]{"Andres Felipe Ruiz",   "andres.ruiz@test.com",    LocalDate.of(2001, 9, 14),  Gender.MALE,   Career.CIBERSEGURIDAD,             7, Position.DEFENDER},
            new Object[]{"Camila Herrera",       "camila.herrera@test.com", LocalDate.of(2002, 5, 19),  Gender.FEMALE, Career.INGENIERIA_DE_SISTEMAS,     7, Position.MIDFIELDER},
            new Object[]{"Santiago Lopez",       "santiago.lopez@test.com", LocalDate.of(2000, 8, 3),   Gender.MALE,   Career.ESTADISTICA,                5, Position.MIDFIELDER},
            new Object[]{"Isabella Moreno",      "isabella.moreno@test.com",LocalDate.of(2003, 12, 11), Gender.FEMALE, Career.INGENIERIA_DE_SISTEMAS,     3, Position.FORWARD}
        );

        int dorsalCounter = 1;
        for (Object[] data : seedData) {
            String mail = (String) data[1];
            if (studentRepository.existsByMailIgnoreCase(mail)) continue;

            UUID userId = UUID.randomUUID();

            StudentEntity student = new StudentEntity();
            student.setId(userId);
            student.setName((String) data[0]);
            student.setMail(mail);
            student.setDateOfBirth((LocalDate) data[2]);
            student.setGender((Gender) data[3]);
            student.setPasswordHash(passwordHash);
            student.setCareer((Career) data[4]);
            student.setSemester((Integer) data[5]);
            student.setPrimaryRole(UserRole.BASEUSER);
            studentRepository.save(student);

            PlayerEntity player = new PlayerEntity();
            player.setId(userId);
            player.setUser(student);
            player.setPosition((Position) data[6]);
            player.setDorsalNumber(dorsalCounter++);
            player.setStatus(PlayerStatus.AVAILABLE);
            playerRepository.save(player);

            log.info("DataSeeder: created player {}", mail);
        }

        log.info("DataSeeder: seeding complete.");
    }
}
