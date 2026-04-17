package com.escuela.techcup.persistence.repository.users;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.escuela.techcup.core.model.enums.UserRole;
import com.escuela.techcup.persistence.entity.users.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);

    @Query("SELECT u FROM UserEntity u JOIN u.roles r WHERE r = :role")
    List<UserEntity> findByRole(@Param("role") UserRole role);
}