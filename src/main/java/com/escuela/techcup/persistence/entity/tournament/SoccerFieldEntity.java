package com.escuela.techcup.persistence.entity.tournament;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "soccer_fields")
public class SoccerFieldEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private String id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "foto")
    private byte[] foto;
}