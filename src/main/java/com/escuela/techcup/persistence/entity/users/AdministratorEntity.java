package com.escuela.techcup.persistence.entity.users;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrators")
public class AdministratorEntity extends UserEntity {
}