package com.escuela.techcup.persistence.entity.users;

import java.util.UUID;

import com.escuela.techcup.core.model.enums.Gender;
import com.escuela.techcup.core.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_mail", columnNames = "mail")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "mail", nullable = false, length = 200)
    private String mail;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 20)
    private Gender gender;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    columnDefinition = "uuid",
                    foreignKey = @ForeignKey(name = "fk_user_roles_user")
            )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<UserRole> roles = new HashSet<>();

    public void addRole(UserRole role){
        roles.add(role);
    }

    public void setPrimaryRole(UserRole PrimaryRole){
        roles.add(PrimaryRole);
    }
}