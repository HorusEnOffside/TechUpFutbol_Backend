package com.escuela.techcup.persistence.repository.tournament;

import java.util.UUID;

import com.escuela.techcup.persistence.entity.tournament.SancionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SancionRepository extends JpaRepository<SancionEntity, UUID> {
}
