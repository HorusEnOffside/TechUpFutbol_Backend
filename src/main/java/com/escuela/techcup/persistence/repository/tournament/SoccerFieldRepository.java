package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;

import java.util.List;

public interface SoccerFieldRepository extends JpaRepository<SoccerFieldEntity, UUID> {

    List<SoccerFieldEntity> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}