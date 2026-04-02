package com.escuela.techcup.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.UserNotFoundException;
import com.escuela.techcup.core.model.User;
import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.core.service.AdminService;
import com.escuela.techcup.persistence.entity.users.UserEntity;
import com.escuela.techcup.persistence.mapper.UserMapper;
import com.escuela.techcup.persistence.repository.users.UserRepository;

@Service
public class AdminServiceImpl implements AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminServiceImpl.class);
    private static final String USER_ID_IS_REQUIRED = "userId is required";
    private static final String ROLE_IS_REQUIRED = "role is required";

    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User assignRole(String userId, UserRole role) {
        if (userId == null || userId.isBlank()) {
            log.warn("Role assignment rejected: userId is null or blank");
            throw new InvalidInputException(USER_ID_IS_REQUIRED);
        }
        if (role == null) {
            log.warn("Role assignment rejected: role is null for userId={}", userId);
            throw new InvalidInputException(ROLE_IS_REQUIRED);
        }

        UserEntity entity = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Role assignment failed: user not found for userId={}", userId);
                    return new UserNotFoundException(userId);
                });

        entity.setPrimaryRole(role);
        userRepository.save(entity);
        log.info("Role assigned successfully. userId={}, role={}", userId, role);

        return UserMapper.toModel(entity);
    }
}