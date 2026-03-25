package com.escuela.techcup.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "administrators")
public class AdministratorEntity extends UserEntity {
}