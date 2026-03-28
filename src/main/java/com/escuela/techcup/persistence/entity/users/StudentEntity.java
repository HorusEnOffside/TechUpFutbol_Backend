package com.escuela.techcup.persistence.entity.users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class StudentEntity extends UserPlayerEntity {

    @Column(name = "semester", nullable = false)
    private Integer semester;
}