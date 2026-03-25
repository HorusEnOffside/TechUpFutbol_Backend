package com.escuela.techcup.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_team_a"))
    private TeamEntity teamA;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_team_b"))
    private TeamEntity teamB;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "soccer_field_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_soccer_field"))
    private SoccerFieldEntity soccerField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", foreignKey = @ForeignKey(name = "fk_matches_referee"))
    private RefereeEntity referee;
}