package com.escuela.techcup.core.service.impl;

import java.awt.image.BufferedImage;

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
import com.escuela.techcup.core.service.UserService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.core.util.PasswordHashUtil;
import com.escuela.techcup.core.util.ValidationUtil;
import com.escuela.techcup.core.validator.UserValidator;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User createAdminUser(UserDTO userDTO) {
        verifyUser(userDTO);
        return new Administrator(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
    }

    @Override
    public User createOrganizerUser(UserDTO userDTO) {
        verifyUser(userDTO);
        return new Organizer(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(),  hashPassword(userDTO.getPassword()));
    }

    @Override
    public User createRefereeUser(UserDTO userDTO) {
        verifyUser(userDTO);
        return new Referee(idGenerator(), userDTO.getName(), userDTO.getMail(), userDTO.getDateOfBirth(), userDTO.getGender(), hashPassword(userDTO.getPassword()));
    }

    @Override
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO) {
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());
        return new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(), 
        studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(), studentUserDTO.getSemester(), hashPassword(studentUserDTO.getPassword()));
    }

    @Override
    public UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture) {
        verifyUser(studentUserDTO);
        ValidationUtil.semesterRules(studentUserDTO.getSemester());
        return new Student(idGenerator(), studentUserDTO.getName(), studentUserDTO.getMail(), profilePicture,
        studentUserDTO.getDateOfBirth(), studentUserDTO.getGender(), studentUserDTO.getSemester(), hashPassword(studentUserDTO.getPassword()));
    }

    @Override
    public UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);
        return new Teacher(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
    }

    @Override
    public UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);
        return new Familiar(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
    }

    @Override
    public UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO) {
        verifyUser(userPlayerDTO);
        return new Graduate(idGenerator(), userPlayerDTO.getName(), userPlayerDTO.getMail(), userPlayerDTO.getDateOfBirth(), userPlayerDTO.getGender(), hashPassword(userPlayerDTO.getPassword()));
    }




    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }
    private String hashPassword(String password) {
        return PasswordHashUtil.hashPassword(password);
    }
    private void verifyUser(UserDTO userDTO) {
        UserValidator.validateInput(userDTO.getName(), userDTO.getMail(), userDTO.getPassword(), userDTO.getDateOfBirth());
    }
}
