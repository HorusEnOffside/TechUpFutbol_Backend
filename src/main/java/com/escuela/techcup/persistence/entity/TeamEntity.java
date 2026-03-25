package com.escuela.techcup.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, length = 120, unique = true)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "logo")
    private byte[] logo;


    @Column(name = "uniform_colors", length = 200)
    private String uniformColors;

    // texto por ahora pq falta implementar el enum de formation
    @Column(name = "formation", length = 50)
    private String formation;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "captain_player_id", foreignKey = @ForeignKey(name = "fk_teams_captain_player"))
    private PlayerEntity captainPlayer;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "fk_teams_payment"))
    private PaymentEntity payment;
}