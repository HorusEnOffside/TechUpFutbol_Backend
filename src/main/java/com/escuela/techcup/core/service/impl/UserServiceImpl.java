package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.model.Administrator;
import com.escuela.techcup.core.model.Familiar;
import com.escuela.techcup.core.model.Graduate;
import com.escuela.techcup.core.model.Organizer;
import com.escuela.techcup.core.model.Referee;
import com.escuela.techcup.core.model.Student;
import com.escuela.techcup.core.model.Teacher;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.UserPlayer;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.core.util.PasswordHashUtil;
import com.escuela.techcup.core.util.ValidationUtil;
import com.escuela.techcup.core.validator.UserValidator;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final List<User> users = new ArrayList<>();

    @Override
    public User createAdminUser(UserDTO userDTO) {
        log.debug("Starting admin user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        User admin = new Administrator(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
        admin.setPrimaryRole(UserRole.ADMIN);
        users.add(admin);
        log.info("Admin user created. userId={}, mail={}", admin.getId(), admin.getMail());
        return admin;
    }

    @Override
    public User createOrganizerUser(UserDTO userDTO) {
        log.debug("Starting organizer user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        User organizer = new Organizer(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(),  hashPassword(userDTO.getPassword()));
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        users.add(organizer);
        log.info("Organizer user created. userId={}, mail={}", organizer.getId(), organizer.getMail());
        return organizer;
    }

    @Override
    public User createRefereeUser(UserDTO userDTO) {
        log.debug("Starting referee user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        User referee = new Referee(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
        referee.setPrimaryRole(UserRole.REFEREE);
        users.add(referee);
        log.info("Referee user created. userId={}, mail={}", referee.getId(), referee.getMail());
        return referee;
    }

    @Override
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO) {
        log.debug("Starting student user creation. mail={}, semester={}", studentUserDTO.getMail(), studentUserDTO.getSemester());
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());
        UserPlayer student = new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(), 
        studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(), studentUserDTO.getSemester(), hashPassword(studentUserDTO.getPassword()));
        users.add(student);
        log.info("Student user created. userId={}, mail={}", student.getId(), student.getMail());
        return student;
    }

    @Override
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture) {
        log.debug("Starting student user creation with profile picture. mail={}, semester={}, hasPhoto={}",
            studentUserDTO.getMail(), studentUserDTO.getSemester(), profilePicture != null);
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());
        UserPlayer student = new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(), profilePicture,
        studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(), studentUserDTO.getSemester(), hashPassword(studentUserDTO.getPassword()));
        users.add(student);
        log.info("Student user with profile picture created. userId={}, mail={}", student.getId(), student.getMail());
        return student;
    }

    @Override
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO) {
        log.debug("Starting teacher user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer teacher = new Teacher(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(teacher);
        log.info("Teacher user created. userId={}, mail={}", teacher.getId(), teacher.getMail());
        return teacher;
    }

    @Override
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting teacher user creation with profile picture. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer teacher = new Teacher(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), profilePicture, userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(teacher);
        log.info("Teacher user with profile picture created. userId={}, mail={}", teacher.getId(), teacher.getMail());
        return teacher;
    }

    @Override
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO) {
        log.debug("Starting familiar user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer familiar = new Familiar(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(familiar);
        log.info("Familiar user created. userId={}, mail={}", familiar.getId(), familiar.getMail());
        return familiar;
    }

    @Override
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting familiar user creation with profile picture. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer familiar = new Familiar(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), profilePicture, userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(familiar);
        log.info("Familiar user with profile picture created. userId={}, mail={}", familiar.getId(), familiar.getMail());
        return familiar;
    }

    @Override
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO) {
        log.debug("Starting graduate user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer graduate = new Graduate(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(graduate);
        log.info("Graduate user created. userId={}, mail={}", graduate.getId(), graduate.getMail());
        return graduate;
    }

    @Override
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting graduate user creation with profile picture. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        UserPlayer graduate = new Graduate(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), profilePicture, userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        users.add(graduate);
        log.info("Graduate user with profile picture created. userId={}, mail={}", graduate.getId(), graduate.getMail());
        return graduate;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public Optional<User> getUserById(String id) {
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst();
    }

    @Override
    public Optional<User> getUserByMail(String mail) {
        return users.stream()
            .filter(user -> user.getMail().equalsIgnoreCase(mail))
            .findFirst();
    }


    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
    private String hashPassword(String password) {
        return PasswordHashUtil.hashPassword(password);
    }
    private void verifyUser(UserDTO userDTO) {
        log.trace("Validating user input. mail={}", userDTO.getMail());
        UserValidator.validateInput(userDTO.getName(), userDTO.getMail(), userDTO.getPassword(), userDTO.getDateOfBirth());
        if (getUserByMail(userDTO.getMail()).isPresent()) {
            throw new InvalidInputException("Ya existe un usuario registrado con ese correo");
        }
        log.trace("User input validation completed. mail={}", userDTO.getMail());
    }
}
