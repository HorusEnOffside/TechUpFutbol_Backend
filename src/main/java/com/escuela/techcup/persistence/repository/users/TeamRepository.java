package com.escuela.techcup.persistence.repository.users;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.TeamEntity;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<TeamEntity, String> {

    Optional<TeamEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}