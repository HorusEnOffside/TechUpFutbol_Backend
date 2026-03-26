package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.TeacherEntity;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {

    Optional<TeacherEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}