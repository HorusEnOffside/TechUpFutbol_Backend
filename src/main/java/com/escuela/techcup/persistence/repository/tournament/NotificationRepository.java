package com.escuela.techcup.persistence.repository.tournament;

import com.escuela.techcup.persistence.entity.tournament.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<NotificationEntity, UUID> {
    boolean existsByUserIdAndReadFalse(UUID userId);
    List<NotificationEntity> findByUserIdOrderByDateTimeDesc(UUID userId);
    List<NotificationEntity> findByUserIdAndReadFalseOrderByDateTimeDesc(UUID userId);
}
