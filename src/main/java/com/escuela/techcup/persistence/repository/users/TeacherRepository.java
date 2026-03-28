package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.TeacherEntity;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<TeacherEntity, String> {

    Optional<TeacherEntity> findByMailIgnoreCase(String mail);

    boolean existsByMailIgnoreCase(String mail);
}