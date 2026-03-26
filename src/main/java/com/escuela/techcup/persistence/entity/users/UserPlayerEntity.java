package com.escuela.techcup.persistence.entity.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_players")
public class UserPlayerEntity extends UserEntity {
}