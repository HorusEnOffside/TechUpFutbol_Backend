package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.UserPlayerEntity;

import java.util.Optional;

public interface UserPlayerRepository extends JpaRepository<UserPlayerEntity, String> {

    Optional<UserPlayerEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}