package com.escuela.techcup.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(
        name = "team_players",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_team_players_player_id", columnNames = "player_id")
        }
)
public class TeamPlayerEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_team"))
    private TeamEntity team;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_player"))
    private PlayerEntity player;
}