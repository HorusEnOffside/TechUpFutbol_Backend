package com.escuela.techcup.persistence.entity.tournament;

import java.util.UUID;

import com.escuela.techcup.core.model.enums.Position;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "lineup_players")
public class LineupPlayerEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "lineup_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lineup_players_lineup"))
    private LineupEntity lineup;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lineup_players_player"))
    private PlayerEntity player;

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false, length = 30)
    private Position position;

    @Column(name = "dorsal_number", nullable = false)
    private int dorsalNumber;
}
