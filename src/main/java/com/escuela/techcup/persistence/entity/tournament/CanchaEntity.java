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

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    /** URL de la imagen estática en el servidor, asignada automáticamente según el tipo. */
    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "tournament_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_canchas_tournament"))
    private TournamentEntity tournament;
}
