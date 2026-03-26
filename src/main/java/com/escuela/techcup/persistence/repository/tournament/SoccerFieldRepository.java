package com.escuela.techcup.persistence.repository.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;

import java.util.List;

public interface SoccerFieldRepository extends JpaRepository<SoccerFieldEntity, String> {

    List<SoccerFieldEntity> findByNameContainingIgnoreCase(String name);
}