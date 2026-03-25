package com.escuela.techcup.persistence.entity;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "teams_amount", nullable = false)
    private int teamsAmount;

    @Column(name = "team_cost", nullable = false)
    private Double teamCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TournamentStatus status;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organizer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tournaments_organizer")
    )
    private OrganizerEntity organizer;
}