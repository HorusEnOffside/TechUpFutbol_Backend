package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sanciones")
public class SancionEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    /** "equipo" o "jugador" */
    @Column(name = "tipo", nullable = false, length = 20)
    private String tipo;

    @Column(name = "entidad_id", nullable = false, columnDefinition = "uuid")
    private String entidadId;

    @Column(name = "entidad_nombre", nullable = false, length = 200)
    private String entidadNombre;

    @Column(name = "motivo", nullable = false, length = 1000)
    private String motivo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @PrePersist
    void prePersist() {
        this.creadoEn = LocalDateTime.now();
    }
}
