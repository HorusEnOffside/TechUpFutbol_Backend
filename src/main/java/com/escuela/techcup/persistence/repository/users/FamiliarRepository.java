package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.FamiliarEntity;

import java.util.Optional;

public interface FamiliarRepository extends JpaRepository<FamiliarEntity, String> {
    Optional<FamiliarEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}