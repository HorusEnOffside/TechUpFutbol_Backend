package com.escuela.techcup.persistence.entity.tournament;

import com.escuela.techcup.core.model.enums.TournamentStatus;
import com.escuela.techcup.persistence.entity.users.OrganizerEntity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    @Column(name = "teams_max_amount", nullable = false)
    private int teamsMaxAmount;

    @Column(name = "team_cost", nullable = false)
    private Double teamCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TournamentStatus status;

    // RF-07: Configuración
    @Column(name = "reglamento", length = 2000)
    private String reglamento;

    @Column(name = "sanciones", length = 500)
    private String sanciones;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CanchaEntity> canchas = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HorarioEntity> horarios = new java.util.ArrayList<>();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "organizer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_tournaments_organizer")
    )
    private OrganizerEntity organizer;

    @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MatchEntity> matches;

    @OneToMany(mappedBy = "tournament", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamEntity> teams;
}