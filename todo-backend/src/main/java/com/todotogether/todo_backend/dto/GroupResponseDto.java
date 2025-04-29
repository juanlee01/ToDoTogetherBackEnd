package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.Group;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupResponseDto {
    private Long id;
    private String title;
    private String createdUsername;
    private LocalDateTime createdAt;

    public GroupResponseDto(Group group) {
        this.id = group.getId();
        this.title = group.getTitle();
        this.createdUsername = group.getCreatedUser().getUsername();
        this.createdAt = group.getCreatedAt();
    }
}
