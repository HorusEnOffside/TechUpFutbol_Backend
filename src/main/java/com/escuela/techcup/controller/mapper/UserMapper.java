package com.escuela.techcup.controller.mapper;

import com.escuela.techcup.controller.dto.UserDTO;
import com.escuela.techcup.core.model.User;

public class UserMapper {

    private UserMapper() {
    }

    public static UserDTO toDTO(User user) {
        return new UserDTO(user.getName(), user.getMail(), user.getDateOfBirth(), user.getGender(), user.getPassword());
    }
}


