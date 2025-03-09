package com.SportSync2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String profilePicture;

    private boolean verified; // Default is false until email verification

    @Enumerated(EnumType.STRING)
    private Role role; // USER, ADMIN
}
