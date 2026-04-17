package com.escuela.techcup.persistence.entity.tournament;

import com.escuela.techcup.core.model.enums.Formation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(
    name = "lineups",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_lineups_match_team", columnNames = {"match_id", "team_id"})
    }
)
public class LineupEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lineups_match"))
    private MatchEntity match;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false, foreignKey = @ForeignKey(name = "fk_lineups_team"))
    private TeamEntity team;

    @Enumerated(EnumType.STRING)
    @Column(name = "formation", nullable = false, length = 30)
    private Formation formation;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "SUBMITTED"; // SUBMITTED, VALIDATED

    @OneToMany(mappedBy = "lineup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineupPlayerEntity> players = new ArrayList<>();
}
