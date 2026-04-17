package com.escuela.techcup.persistence.entity.tournament;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.escuela.techcup.persistence.entity.users.PlayerEntity;

@Getter
@Setter
@Entity
@Table(name = "goals")
public class GoalEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "minute", nullable = false)
    private int minute;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, foreignKey = @ForeignKey(name = "fk_goals_match"))
    private MatchEntity match;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(name = "fk_goals_player"))
    private PlayerEntity player;
}