package com.todotogether.todo_backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String body;

    @ManyToOne
    @JoinColumn(name = "created_id", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group; // null일 경우 개인 Todo로 간주

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TodoStatus status;

    private String tag;

    private LocalDateTime createdAt;

    // getters, setters
}

