package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.UserPlayerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPlayerRepository extends JpaRepository<UserPlayerEntity, UUID> {

    Optional<UserPlayerEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}