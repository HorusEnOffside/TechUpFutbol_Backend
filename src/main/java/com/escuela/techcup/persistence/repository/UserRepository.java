package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}