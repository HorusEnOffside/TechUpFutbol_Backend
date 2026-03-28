package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.users.AdministratorEntity;

import java.util.Optional;

public interface AdministratorRepository extends JpaRepository<AdministratorEntity, String> {
    Optional<AdministratorEntity> findByMailIgnoreCase(String mail);
    boolean existsByMailIgnoreCase(String mail);
}