package com.escuela.techcup.persistence.entity.tournament;

import com.escuela.techcup.persistence.entity.users.RefereeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_team_a"))
    private TeamEntity teamA;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_team_b"))
    private TeamEntity teamB;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "soccer_field_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_soccer_field"))
    private SoccerFieldEntity soccerField;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", foreignKey = @ForeignKey(name = "fk_matches_referee"))
    private RefereeEntity referee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false, foreignKey = @ForeignKey(name = "fk_matches_tournament"))
    private TournamentEntity tournament;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GoalEntity> goals = new ArrayList<>();

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CardEntity> cards = new ArrayList<>();

    @Column(name = "status", nullable = false)
    private String status = "PENDING";

    @Column(name = "local_score", nullable = false)
    private int localScore = 0;

    @Column(name = "visitor_score", nullable = false)
    private int visitorScore = 0;

    public void addGoal(GoalEntity goal) {
        goals.add(goal);
        goal.setMatch(this);
    }

    public void addCard(CardEntity card) {
        cards.add(card);
        card.setMatch(this);
    }

}
