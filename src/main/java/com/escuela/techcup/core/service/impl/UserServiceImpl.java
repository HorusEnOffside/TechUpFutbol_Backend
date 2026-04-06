package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.escuela.techcup.core.model.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.GraduateUserDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.TeacherUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.*;
import com.escuela.techcup.core.util.*;
import com.escuela.techcup.core.validator.StudentValidator;
import com.escuela.techcup.core.validator.UserValidator;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.mapper.users.AdminMapper;
import com.escuela.techcup.persistence.mapper.users.FamiliarMapper;
import com.escuela.techcup.persistence.mapper.users.GraduateMapper;
import com.escuela.techcup.persistence.mapper.users.OrganizerMapper;
import com.escuela.techcup.persistence.mapper.users.RefereeMapper;
import com.escuela.techcup.persistence.mapper.users.StudentMapper;
import com.escuela.techcup.persistence.mapper.users.TeacherMapper;
import com.escuela.techcup.persistence.mapper.users.UserMapper;
import com.escuela.techcup.persistence.repository.users.*;

@Service
public class UserServiceImpl implements com.escuela.techcup.core.service.UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String USER_DTO_IS_REQUIRED = "User data is required";
    private static final String USER_ID_IS_REQUIRED = "id is required";
    private static final String USER_MAIL_IS_REQUIRED = "mail is required";

    private final UserRepository userRepository;
    private final AdministratorRepository administratorRepository;
    private final OrganizerRepository organizerRepository;
    private final RefereeRepository refereeRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final FamiliarRepository familiarRepository;
    private final GraduateRepository graduateRepository;

    public UserServiceImpl(
            UserRepository userRepository,
            AdministratorRepository administratorRepository,
            OrganizerRepository organizerRepository,
            RefereeRepository refereeRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            FamiliarRepository familiarRepository,
            GraduateRepository graduateRepository
    ) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.organizerRepository = organizerRepository;
        this.refereeRepository = refereeRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.familiarRepository = familiarRepository;
        this.graduateRepository = graduateRepository;
    }

    @Override
    @Transactional
    public User createAdminUser(UserDTO userDTO, BufferedImage profilePicture) {
        log.debug("Starting admin user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        Administrator admin = new Administrator(idGenerator(), userDTO.getName(), userDTO.getMail(),
                userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
        admin.setPrimaryRole(UserRole.ADMIN);
        if (profilePicture != null) admin.setProfilePicture(profilePicture);
        administratorRepository.save(AdminMapper.toEntity(admin));
        log.info("Admin user created successfully. mail={}", admin.getMail());
        return admin;
    }

    @Override
    @Transactional
    public User createOrganizerUser(UserDTO userDTO, BufferedImage profilePicture) {
        log.debug("Starting organizer user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        Organizer organizer = new Organizer(idGenerator(), userDTO.getName(), userDTO.getMail(),
                userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        if (profilePicture != null) organizer.setProfilePicture(profilePicture);
        organizerRepository.save(OrganizerMapper.toEntity(organizer));
        log.info("Organizer user created successfully. mail={}", organizer.getMail());
        return organizer;
    }

    @Override
    @Transactional
    public User createRefereeUser(UserDTO userDTO, BufferedImage profilePicture) {
        log.debug("Starting referee user creation. mail={}", userDTO.getMail());
        verifyUser(userDTO);
        Referee referee = new Referee(idGenerator(), userDTO.getName(), userDTO.getMail(),
                userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
        referee.setPrimaryRole(UserRole.REFEREE);
        if (profilePicture != null) referee.setProfilePicture(profilePicture);
        refereeRepository.save(RefereeMapper.toEntity(referee));
        log.info("Referee user created successfully. mail={}", referee.getMail());
        return referee;
    }

    @Override
    @Transactional
    public UserPlayer createTeacherUser(TeacherUserDTO teacherUserDTO, BufferedImage profilePicture) {
        log.debug("Starting teacher user creation. mail={}", teacherUserDTO.getMail());
        verifyUser(teacherUserDTO);
        Teacher teacher = new Teacher(idGenerator(), teacherUserDTO.getName(), teacherUserDTO.getMail(),
                teacherUserDTO.getDateOfBirth(), teacherUserDTO.getGender(),
                hashPassword(teacherUserDTO.getPassword()), teacherUserDTO.getCareer());
        if (profilePicture != null) teacher.setProfilePicture(profilePicture);
        teacherRepository.save(TeacherMapper.toEntity(teacher));
        log.info("Teacher user created successfully. mail={}", teacher.getMail());
        return teacher;
    }

    @Override
    @Transactional
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting familiar user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        Familiar familiar = new Familiar(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(),
                userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        if (profilePicture != null) familiar.setProfilePicture(profilePicture);
        familiarRepository.save(FamiliarMapper.toEntity(familiar));
        log.info("Familiar user created successfully. mail={}", familiar.getMail());
        return familiar;
    }

    @Override
    @Transactional
    public UserPlayer createGraduateUser(GraduateUserDTO graduateUserDTO, BufferedImage profilePicture) {
        log.debug("Starting graduate user creation. mail={}", graduateUserDTO.getMail());
        verifyUser(graduateUserDTO);
        Graduate graduate = new Graduate(idGenerator(), graduateUserDTO.getName(), graduateUserDTO.getMail(),
                graduateUserDTO.getDateOfBirth(), graduateUserDTO.getGender(),
                hashPassword(graduateUserDTO.getPassword()), graduateUserDTO.getCareer());
        if (profilePicture != null) graduate.setProfilePicture(profilePicture);
        graduateRepository.save(GraduateMapper.toEntity(graduate));
        log.info("Graduate user created successfully. mail={}", graduate.getMail());
        return graduate;
    }

    @Override
    @Transactional
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture) {
        log.debug("Starting student user creation. mail={}", studentUserDTO.getMail());
        verifyUserStudent(studentUserDTO);
        Student student = new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(),
                studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(),
                studentUserDTO.getSemester(), studentUserDTO.getCareer(),
                hashPassword(studentUserDTO.getPassword()));
        if (profilePicture != null) student.setProfilePicture(profilePicture);
        studentRepository.save(StudentMapper.toEntity(student));
        log.info("Student user created successfully. mail={}", student.getMail());
        return student;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toModel)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(String id) {
        if (id == null || id.isBlank()) throw new InvalidInputException(USER_ID_IS_REQUIRED);
        return userRepository.findById(id).map(UserMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByMail(String mail) {
        if (mail == null || mail.isBlank()) throw new InvalidInputException(USER_MAIL_IS_REQUIRED);
        return userRepository.findByMailIgnoreCase(mail).map(UserMapper::toModel);
    }

    private String idGenerator() { return IdGeneratorUtil.generateId(); }
    private String hashPassword(String password) { return PasswordHashUtil.hashPassword(password); }

    private void verifyUser(UserDTO userDTO) {
        if (userDTO == null) throw new InvalidInputException(USER_DTO_IS_REQUIRED);
        UserValidator.validateInput(userDTO.getName(), userDTO.getMail(), userDTO.getPassword(), userDTO.getDateOfBirth());
        if (userRepository.existsByMailIgnoreCase(userDTO.getMail()))
            throw new InvalidInputException("A user is already registered with that email");
    }

    private void verifyUserStudent(StudentUserDTO studentUserDTO) {
        if (studentUserDTO == null) throw new InvalidInputException(USER_DTO_IS_REQUIRED);
        StudentValidator.validateInput(studentUserDTO.getName(), studentUserDTO.getMail(),
                studentUserDTO.getPassword(), studentUserDTO.getDateOfBirth(), studentUserDTO.getSemester());
        if (userRepository.existsByMailIgnoreCase(studentUserDTO.getMail()))
            throw new InvalidInputException("A user is already registered with that email");
    }

    private byte[] toPngBytes(BufferedImage image) {
        if (image == null) return new byte[0];
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new InvalidInputException("Could not encode profile picture");
        }
    }
}