package com.escuela.techcup.persistence.repository.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;

import java.util.Optional;

public interface UserPlayerRepository extends JpaRepository<UserPlayerEntity, UUID> {

    Optional<UserPlayerEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}