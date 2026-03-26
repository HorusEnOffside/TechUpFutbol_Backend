package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}