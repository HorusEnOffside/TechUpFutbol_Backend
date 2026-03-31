package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import com.escuela.techcup.core.model.enums.Formation;
import com.escuela.techcup.persistence.entity.payment.PaymentEntity;
import com.escuela.techcup.persistence.entity.users.PlayerEntity;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 120, unique = true)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "logo")
    private byte[] logo;

    //guarda hexadecimal
    @Column(name = "uniform_color", length = 200)
    private String uniformColor;

    @Enumerated(EnumType.STRING)
    @Column(name = "formation", length = 50)
    private Formation formation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_player_id", foreignKey = @ForeignKey(name = "fk_teams_captain_player"))
    private PlayerEntity captainPlayer;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_teams_payment"))
    private PaymentEntity payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false, foreignKey = @ForeignKey(name = "fk_teams_tournament"))
    private TournamentEntity tournament;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TeamPlayerEntity> players;
}