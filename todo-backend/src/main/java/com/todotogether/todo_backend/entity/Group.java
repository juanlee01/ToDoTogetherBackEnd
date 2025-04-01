package com.todotogether.todo_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "groups")
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "createduser_id")
    private User createdUser;

    private LocalDateTime createdAt;

    // getters, setters
}
