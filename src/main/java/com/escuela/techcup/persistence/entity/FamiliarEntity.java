package com.escuela.techcup.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "familiars")
public class FamiliarEntity extends UserPlayerEntity {
}