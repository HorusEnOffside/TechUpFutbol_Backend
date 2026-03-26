package com.escuela.techcup.persistence.entity.users;

import java.util.List;

import com.escuela.techcup.persistence.entity.tournament.TournamentEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "organizers")
public class OrganizerEntity extends UserEntity {

    @OneToMany(mappedBy = "organizer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TournamentEntity> tournaments;
}