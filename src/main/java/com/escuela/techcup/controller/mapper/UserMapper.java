package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.UserResponseDTO;
import com.escuela.techcup.core.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(user.getName(), user.getMail(), user.getDateOfBirth(), user.getGender());
    }
}


