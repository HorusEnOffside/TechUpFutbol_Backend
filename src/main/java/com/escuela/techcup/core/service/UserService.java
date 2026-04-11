package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

import com.escuela.techcup.controller.dto.GraduateUserDTO;
import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.TeacherUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.UserPlayer;

public interface UserService {

    User createAdminUser(UserDTO userDTO, BufferedImage profilePicture);
    User createOrganizerUser(UserDTO userDTO, BufferedImage profilePicture);
    User createRefereeUser(UserDTO userDTO, BufferedImage profilePicture);

    UserPlayer createTeacherUser(TeacherUserDTO teacherUserDTO, BufferedImage profilePicture);
    UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture);
    UserPlayer createGraduateUser(GraduateUserDTO graduateUserDTO, BufferedImage profilePicture);

    UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture);

    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    Optional<User> getUserByMail(String mail);
}