package com.neodohae_spring_boot.neodohae_spring_boot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email", "provider"})
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('MALE','FEMALE','UNDEFINED')")
    private Gender gender = Gender.UNDEFINED;

    @Column(name = "birthYear")
    private int birthYear;

    @Column(name = "picture")
    private String picture;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "color")
    private String color;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;


    // One to Many Mapping
    // multiple users to one room
    @ManyToOne
    @JoinColumn(name = "roomId")
    private Room room;
}

enum Gender {
    MALE,
    FEMALE,
    UNDEFINED
}