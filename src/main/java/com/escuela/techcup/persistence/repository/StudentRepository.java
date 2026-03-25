package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<StudentEntity, UUID> {

    Optional<StudentEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}