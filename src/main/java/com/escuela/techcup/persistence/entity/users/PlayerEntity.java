package com.escuela.techcup.persistence.entity.users;

import com.escuela.techcup.core.model.enums.PlayerStatus;
import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.tournament.TeamEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(
        name = "players",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_players_user_id", columnNames = "user_id")
        }
)
public class PlayerEntity {

    @Id
    @Column(name = "id")
    private String id;

    // 1-1: un user_player solo puede tener 1 perfil deportivo
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_players_user"))
    private UserPlayerEntity user;

    @Column(name = "dorsal_number", nullable = false)
    private int dorsalNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, length = 30)
    private Position position;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PlayerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "fk_players_team"))
    private TeamEntity team;
}