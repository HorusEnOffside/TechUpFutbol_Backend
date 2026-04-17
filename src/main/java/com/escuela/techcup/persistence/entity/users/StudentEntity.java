package com.escuela.techcup.persistence.entity.users;

import com.escuela.techcup.core.model.enums.Career;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class StudentEntity extends UserPlayerEntity {

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Enumerated(EnumType.STRING)
    @Column(name = "career", nullable = false, length = 50)
    private Career career;
}