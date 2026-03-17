package com.escuela.techcup.core.service;

import java.awt.image.BufferedImage;

import com.escuela.techcup.controller.dto.StudentUserDTO;
import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.controller.dto.UserPlayerDTO;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.UserPlayer;

public interface UserService {

    User createAdminUser(UserDTO userDTO);
    User createOrganizerUser(UserDTO userDTO);
    User createRefereeUser(UserDTO userDTO);

    UserPlayer createStudentUser(StudentUserDTO studentUserDTO);
    UserPlayer createStudentUser(StudentUserDTO studentUserDTO, BufferedImage profilePicture);
    UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO);
    UserPlayer createTeacherUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture);
    UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO);
    UserPlayer createFamiliarUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture);
    UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO);
    UserPlayer createGraduateUser(UserPlayerDTO userPlayerDTO, BufferedImage profilePicture);
}
