package com.escuela.techcup.core.scheduler;

import com.escuela.techcup.core.model.Notification;
import com.escuela.techcup.core.model.enums.NotificationType;
import com.escuela.techcup.core.service.NotificationService;
import com.escuela.techcup.persistence.entity.tournament.MatchEntity;
import com.escuela.techcup.persistence.repository.tournament.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MatchReminderScheduler {

    private static final Logger log = LoggerFactory.getLogger(MatchReminderScheduler.class);

    private final MatchRepository matchRepository;
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 60_000)
    public void sendMatchReminders() {
        LocalDateTime from = LocalDateTime.now().plusHours(2);
        LocalDateTime to = from.plusMinutes(1);

        List<MatchEntity> upcomingMatches = matchRepository.findByDateTimeBetween(from, to);

        for (MatchEntity match : upcomingMatches) {
            notifyCaptain(match, match.getTeamA());
            notifyCaptain(match, match.getTeamB());
        }

        if (!upcomingMatches.isEmpty()) {
            log.info("Match reminders sent for {} matches", upcomingMatches.size());
        }
    }

    private void notifyCaptain(MatchEntity match, com.escuela.techcup.persistence.entity.tournament.TeamEntity team) {
        if (team == null || team.getCaptainPlayer() == null) return;
        UUID captainUserId = team.getCaptainPlayer().getUser().getId();

        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .userId(captainUserId)
                .type(NotificationType.MATCH_REMINDER)
                .title("¡Última hora para hacer cambios!")
                .description("Tu partido comienza en 2 horas. Es la última oportunidad para cambiar formación o alineación.")
                .relatedId(match.getId())
                .dateTime(LocalDateTime.now())
                .read(false)
                .build();

        notificationService.createNotification(notification);
    }
}
