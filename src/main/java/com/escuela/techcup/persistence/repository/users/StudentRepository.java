package com.escuela.techcup.persistence.repository.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.StudentEntity;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {

    Optional<StudentEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}