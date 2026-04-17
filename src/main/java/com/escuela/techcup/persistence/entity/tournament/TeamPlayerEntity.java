package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.escuela.techcup.persistence.entity.users.PlayerEntity;

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
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_team"))
    private TeamEntity team;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(name = "fk_team_players_player"))
    private PlayerEntity player;
}