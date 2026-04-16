package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "canchas")
public class CanchaEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto")
    private byte[] foto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_canchas_tournament"))
    private TournamentEntity tournament;
}
