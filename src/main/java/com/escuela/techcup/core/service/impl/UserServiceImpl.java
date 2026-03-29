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

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.model.*;
import com.escuela.techcup.core.util.*;
import com.escuela.techcup.core.validator.StudentValidator;
import com.escuela.techcup.core.validator.UserValidator;
import com.escuela.techcup.persistence.entity.users.*;
import com.escuela.techcup.persistence.mapper.*;
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

        if (profilePicture != null) {
            admin.setProfilePicture(profilePicture);
        }

        AdministratorEntity entity = AdminMapper.toEntity(admin);
        administratorRepository.save(entity);
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

        if (profilePicture != null) {
            organizer.setProfilePicture(profilePicture);
        }

        OrganizerEntity entity = OrganizerMapper.toEntity(organizer);
        organizerRepository.save(entity);
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

        if (profilePicture != null) {
            referee.setProfilePicture(profilePicture);
        }

        RefereeEntity entity = RefereeMapper.toEntity(referee);
        refereeRepository.save(entity);
        log.info("Referee user created successfully. mail={}", referee.getMail());
        return referee;
    }

    @Override
    @Transactional
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting teacher user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        
        Teacher teacher = new Teacher(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));

        if (profilePicture != null) {
            log.debug("Profile picture provided for teacher user. mail={}", userPlayerDTO.getMail());
            teacher.setProfilePicture(profilePicture);
        } else {
            log.debug("No profile picture provided for teacher user. mail={}", userPlayerDTO.getMail());
        }

        TeacherEntity entity = TeacherMapper.toEntity(teacher);
        log.debug("Saving teacher user. mail={}", teacher.getMail());
        teacherRepository.save(entity);
        log.debug("Teacher user saved successfully. mail={}", teacher.getMail());
        return teacher;
    }

    @Override
    @Transactional
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting familiar user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        
        Familiar familiar = new Familiar(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
        if (profilePicture != null) {
            log.debug("Profile picture provided for familiar user. mail={}", userPlayerDTO.getMail());
            familiar.setProfilePicture(profilePicture);
        } else {
            log.debug("No profile picture provided for familiar user. mail={}", userPlayerDTO.getMail());
        }

        FamiliarEntity entity = FamiliarMapper.toEntity(familiar);
        log.debug("Saving familiar user. mail={}", familiar.getMail());
        familiarRepository.save(entity);
        log.debug("Familiar user saved successfully. mail={}", familiar.getMail());
        return familiar;
    }

    @Override
    @Transactional
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        log.debug("Starting graduate user creation. mail={}", userPlayerDTO.getMail());
        verifyUser(userPlayerDTO);
        
        Graduate graduate = new Graduate(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));

        if (profilePicture != null) {
            log.debug("Profile picture provided for graduate user. mail={}", userPlayerDTO.getMail());
            graduate.setProfilePicture(profilePicture);
        } else {
            log.debug("No profile picture provided for graduate user. mail={}", userPlayerDTO.getMail());
        }

        GraduateEntity entity = GraduateMapper.toEntity(graduate);
        log.debug("Saving graduate user. mail={}", graduate.getMail());
        graduateRepository.save(entity);
        log.debug("Graduate user saved successfully. mail={}", graduate.getMail());
        return graduate;
    }

    @Override
    @Transactional
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture) {
        log.debug("Starting student user creation. mail={}", studentUserDTO.getMail());
        verifyUserStudent(studentUserDTO);
        

        Student student = new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(), studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(), studentUserDTO.getSemester(), hashPassword(studentUserDTO.getPassword()));

        if (profilePicture != null) {
            log.debug("Profile picture provided for student user. mail={}", studentUserDTO.getMail());
            student.setProfilePicture(profilePicture);
        } else {
            log.debug("No profile picture provided for student user. mail={}", studentUserDTO.getMail());
        }

        StudentEntity entity = StudentMapper.toEntity(student);
        log.debug("Saving student user. mail={}", student.getMail());
        studentRepository.save(entity);
        log.debug("Student user saved successfully. mail={}", student.getMail());
        return student;
    }


    @Override
    @Transactional(readOnly = true) //dto response 
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toModel)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(String id) {
        if (id == null || id.isBlank()) {
            log.warn("Cannot search user by empty id");
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }
        return userRepository.findById(id)
                .map(UserMapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByMail(String mail) {
        if (mail == null || mail.isBlank()) {
            log.warn("Cannot search user by empty mail");
            throw new InvalidInputException(USER_MAIL_IS_REQUIRED);
        }
        return userRepository.findByMailIgnoreCase(mail)
                .map(UserMapper::toModel);
    }

    
    
    //--------------------------------
    //helper methods

    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
    private String hashPassword(String password) {
        return PasswordHashUtil.hashPassword(password);
    }

    private void verifyUser(UserDTO userDTO) {
        if (userDTO == null) {
            log.warn("User creation rejected: payload is null");
            throw new InvalidInputException(USER_DTO_IS_REQUIRED);
        }
        UserValidator.validateInput(userDTO.getName(), userDTO.getMail(), userDTO.getPassword(), userDTO.getDateOfBirth());
        if (userRepository.existsByMailIgnoreCase(userDTO.getMail())) {
            log.warn("User already exists for mail={}", userDTO.getMail());
            throw new InvalidInputException("A user is already registered with that email");
        }
        log.trace("User input validation completed. mail={}", userDTO.getMail());
    }

    private void verifyUserStudent(StudentUserDTO studentUserDTO) {
        if (studentUserDTO == null) {
            log.warn("User creation rejected: payload is null");
            throw new InvalidInputException(USER_DTO_IS_REQUIRED);
        }
        StudentValidator.validateInput(studentUserDTO.getName(), studentUserDTO.getMail(), studentUserDTO.getPassword(), studentUserDTO.getDateOfBirth(), studentUserDTO.getSemester());
        if (userRepository.existsByMailIgnoreCase(studentUserDTO.getMail())) {
            log.warn("User already exists for mail={}", studentUserDTO.getMail());
            throw new InvalidInputException("A user is already registered with that email");
        }
        log.trace("User input validation completed. mail={}", studentUserDTO.getMail());
    }


    private byte[] toPngBytes(BufferedImage image) {
        if (image == null) {
            return new byte[0];
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.warn("Could not encode profile picture");
            throw new InvalidInputException("Could not encode profile picture");
        }
    }
}