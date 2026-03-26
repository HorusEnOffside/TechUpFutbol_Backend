package com.escuela.techcup.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;

import java.util.List;
import java.util.UUID;

public interface SoccerFieldRepository extends JpaRepository<SoccerFieldEntity, UUID> {

    List<SoccerFieldEntity> findByNameContainingIgnoreCase(String name);
}