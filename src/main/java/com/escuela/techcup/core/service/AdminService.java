package com.escuela.techcup.core.service;

import java.util.List;
import java.util.Optional;

import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.enums.UserRole;

public interface AdminService {
    User assignRole(String userId, UserRole role);
}