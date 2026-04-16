package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    boolean existsByUserIdAndReadFalse(Long userId);
    List<NotificationEntity> findByUserIdAndReadFalse(Long userId);
}
