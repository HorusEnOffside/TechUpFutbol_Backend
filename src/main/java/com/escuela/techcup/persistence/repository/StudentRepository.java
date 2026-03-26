package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.StudentEntity;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {

    Optional<StudentEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}