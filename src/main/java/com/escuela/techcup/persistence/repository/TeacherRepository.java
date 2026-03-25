package com.escuela.techcup.persistence.repository;

import com.escuela.techcup.persistence.entity.TeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<TeacherEntity, UUID> {

    Optional<TeacherEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}