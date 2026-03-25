package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.exception.InvalidInputException;
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
import com.escuela.techcup.core.util.ValidationUtil;
import com.escuela.techcup.core.validator.UserValidator;
import com.escuela.techcup.persistence.entity.AdministratorEntity;
import com.escuela.techcup.persistence.entity.FamiliarEntity;
import com.escuela.techcup.persistence.entity.GraduateEntity;
import com.escuela.techcup.persistence.entity.OrganizerEntity;
import com.escuela.techcup.persistence.entity.RefereeEntity;
import com.escuela.techcup.persistence.entity.StudentEntity;
import com.escuela.techcup.persistence.entity.TeacherEntity;
import com.escuela.techcup.persistence.entity.UserEntity;
import com.escuela.techcup.persistence.repository.AdministratorRepository;
import com.escuela.techcup.persistence.repository.FamiliarRepository;
import com.escuela.techcup.persistence.repository.GraduateRepository;
import com.escuela.techcup.persistence.repository.OrganizerRepository;
import com.escuela.techcup.persistence.repository.RefereeRepository;
import com.escuela.techcup.persistence.repository.StudentRepository;
import com.escuela.techcup.persistence.repository.TeacherRepository;
import com.escuela.techcup.persistence.repository.UserRepository;

@Service
public class UserServiceImpl implements com.escuela.techcup.core.service.UserService {

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
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            AdministratorRepository administratorRepository,
            OrganizerRepository organizerRepository,
            RefereeRepository refereeRepository,
            StudentRepository studentRepository,
            TeacherRepository teacherRepository,
            FamiliarRepository familiarRepository,
            GraduateRepository graduateRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.administratorRepository = administratorRepository;
        this.organizerRepository = organizerRepository;
        this.refereeRepository = refereeRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.familiarRepository = familiarRepository;
        this.graduateRepository = graduateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createAdminUser(UserDTO userDTO) {
        verifyUser(userDTO);

        AdministratorEntity entity = new AdministratorEntity();
        entity.setName(userDTO.getName());
        entity.setMail(userDTO.getMail());
        entity.setDateOfBirth(userDTO.getDateOfBirth());
        entity.setGender(userDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        entity.getRoles().add(UserRole.ADMIN);

        AdministratorEntity saved = administratorRepository.save(entity);

        Administrator admin = new Administrator(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        admin.setPrimaryRole(UserRole.ADMIN);
        admin.getRoles().addAll(saved.getRoles());
        return admin;
    }

    @Override
    @Transactional
    public User createOrganizerUser(UserDTO userDTO) {
        verifyUser(userDTO);

        OrganizerEntity entity = new OrganizerEntity();
        entity.setName(userDTO.getName());
        entity.setMail(userDTO.getMail());
        entity.setDateOfBirth(userDTO.getDateOfBirth());
        entity.setGender(userDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        entity.getRoles().add(UserRole.ORGANIZER);

        OrganizerEntity saved = organizerRepository.save(entity);

        Organizer organizer = new Organizer(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        organizer.setPrimaryRole(UserRole.ORGANIZER);
        organizer.getRoles().addAll(saved.getRoles());
        return organizer;
    }

    @Override
    @Transactional
    public User createRefereeUser(UserDTO userDTO) {
        verifyUser(userDTO);

        RefereeEntity entity = new RefereeEntity();
        entity.setName(userDTO.getName());
        entity.setMail(userDTO.getMail());
        entity.setDateOfBirth(userDTO.getDateOfBirth());
        entity.setGender(userDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        entity.getRoles().add(UserRole.REFEREE);

        RefereeEntity saved = refereeRepository.save(entity);

        Referee referee = new Referee(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        referee.setPrimaryRole(UserRole.REFEREE);
        referee.getRoles().addAll(saved.getRoles());
        return referee;
    }

    @Override
    @Transactional
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO) {
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());

        StudentEntity entity = new StudentEntity();
        entity.setName(studentUserDTO.getName());
        entity.setMail(studentUserDTO.getMail());
        entity.setDateOfBirth(studentUserDTO.getDateOfBirth());
        entity.setGender(studentUserDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(studentUserDTO.getPassword()));
        entity.setSemester(studentUserDTO.getSemester());
        entity.getRoles().add(UserRole.BASEUSER);

        StudentEntity saved = studentRepository.save(entity);

        Student student = new Student(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getSemester(),
                saved.getPasswordHash()
        );
        student.setPrimaryRole(UserRole.BASEUSER);
        student.getRoles().addAll(saved.getRoles());
        return student;
    }

    @Override
    @Transactional
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture) {
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());

        StudentEntity entity = new StudentEntity();
        entity.setName(studentUserDTO.getName());
        entity.setMail(studentUserDTO.getMail());
        entity.setDateOfBirth(studentUserDTO.getDateOfBirth());
        entity.setGender(studentUserDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(studentUserDTO.getPassword()));
        entity.setSemester(studentUserDTO.getSemester());
        entity.setProfilePicture(toPngBytes(profilePicture));
        entity.getRoles().add(UserRole.BASEUSER);

        StudentEntity saved = studentRepository.save(entity);

        Student student = new Student(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                profilePicture,
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getSemester(),
                saved.getPasswordHash()
        );
        student.setPrimaryRole(UserRole.BASEUSER);
        student.getRoles().addAll(saved.getRoles());
        return student;
    }

    @Override
    @Transactional
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);

        TeacherEntity entity = new TeacherEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.getRoles().add(UserRole.BASEUSER);

        TeacherEntity saved = teacherRepository.save(entity);

        Teacher teacher = new Teacher(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        teacher.setPrimaryRole(UserRole.BASEUSER);
        teacher.getRoles().addAll(saved.getRoles());
        return teacher;
    }

    @Override
    @Transactional
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        verifyUser(userPlayerDTO);

        TeacherEntity entity = new TeacherEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.setProfilePicture(toPngBytes(profilePicture));
        entity.getRoles().add(UserRole.BASEUSER);

        TeacherEntity saved = teacherRepository.save(entity);

        Teacher teacher = new Teacher(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                profilePicture,
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        teacher.setPrimaryRole(UserRole.BASEUSER);
        teacher.getRoles().addAll(saved.getRoles());
        return teacher;
    }

    @Override
    @Transactional
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);

        FamiliarEntity entity = new FamiliarEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.getRoles().add(UserRole.BASEUSER);

        FamiliarEntity saved = familiarRepository.save(entity);

        Familiar familiar = new Familiar(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        familiar.setPrimaryRole(UserRole.BASEUSER);
        familiar.getRoles().addAll(saved.getRoles());
        return familiar;
    }

    @Override
    @Transactional
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        verifyUser(userPlayerDTO);

        FamiliarEntity entity = new FamiliarEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.setProfilePicture(toPngBytes(profilePicture));
        entity.getRoles().add(UserRole.BASEUSER);

        FamiliarEntity saved = familiarRepository.save(entity);

        Familiar familiar = new Familiar(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                profilePicture,
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        familiar.setPrimaryRole(UserRole.BASEUSER);
        familiar.getRoles().addAll(saved.getRoles());
        return familiar;
    }

    @Override
    @Transactional
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);

        GraduateEntity entity = new GraduateEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.getRoles().add(UserRole.BASEUSER);

        GraduateEntity saved = graduateRepository.save(entity);

        Graduate graduate = new Graduate(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        graduate.setPrimaryRole(UserRole.BASEUSER);
        graduate.getRoles().addAll(saved.getRoles());
        return graduate;
    }

    @Override
    @Transactional
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture) {
        verifyUser(userPlayerDTO);

        GraduateEntity entity = new GraduateEntity();
        entity.setName(userPlayerDTO.getName());
        entity.setMail(userPlayerDTO.getMail());
        entity.setDateOfBirth(userPlayerDTO.getDateOfBirth());
        entity.setGender(userPlayerDTO.getGender());
        entity.setPasswordHash(passwordEncoder.encode(userPlayerDTO.getPassword()));
        entity.setProfilePicture(toPngBytes(profilePicture));
        entity.getRoles().add(UserRole.BASEUSER);

        GraduateEntity saved = graduateRepository.save(entity);

        Graduate graduate = new Graduate(
                saved.getId().toString(),
                saved.getName(),
                saved.getMail(),
                profilePicture,
                saved.getDateOfBirth(),
                saved.getGender(),
                saved.getPasswordHash()
        );
        graduate.setPrimaryRole(UserRole.BASEUSER);
        graduate.getRoles().addAll(saved.getRoles());
        return graduate;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(this::toCoreUser).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }
        UUID uuid = UUID.fromString(id);
        return userRepository.findById(uuid).map(this::toCoreUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserByMail(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new InvalidInputException(USER_MAIL_IS_REQUIRED);
        }
        return userRepository.findByMailIgnoreCase(mail).map(this::toCoreUser);
    }

    private void verifyUser(UserDTO userDTO) {
        if (userDTO == null) {
            throw new InvalidInputException(USER_DTO_IS_REQUIRED);
        }
        UserValidator.validateInput(userDTO.getName(), userDTO.getMail(), userDTO.getPassword(), userDTO.getDateOfBirth());
        if (userRepository.existsByMailIgnoreCase(userDTO.getMail())) {
            throw new InvalidInputException("A user is already registered with that email");
        }
    }

    private User toCoreUser(UserEntity e) {
        if (e instanceof AdministratorEntity) {
            Administrator u = new Administrator(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof OrganizerEntity) {
            Organizer u = new Organizer(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof RefereeEntity) {
            Referee u = new Referee(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof StudentEntity se) {
            Student u = new Student(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), se.getSemester(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof TeacherEntity) {
            Teacher u = new Teacher(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof FamiliarEntity) {
            Familiar u = new Familiar(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        if (e instanceof GraduateEntity) {
            Graduate u = new Graduate(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
            u.getRoles().addAll(e.getRoles());
            return u;
        }
        UserPlayer u = new UserPlayer(e.getId().toString(), e.getName(), e.getMail(), e.getDateOfBirth(), e.getGender(), e.getPasswordHash());
        u.getRoles().addAll(e.getRoles());
        return u;
    }

    private byte[] toPngBytes(BufferedImage image) {
        if (image == null) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new InvalidInputException("Could not encode profile picture");
        }
    }
}