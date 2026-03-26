package com.escuela.techcup.persistence.entity.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "referees")
public class RefereeEntity extends UserEntity {
}