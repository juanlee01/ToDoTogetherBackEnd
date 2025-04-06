package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.Todo;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class TodoResponseDto {
    private Long id;
    private String title;
    private String body;
    private String status;
    private String tag;
    private String createdAt;
    private SimpleUserDto createdBy;
    private SimpleUserDto assignedTo;

    public TodoResponseDto(Todo todo) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.body = todo.getBody();
        this.status = todo.getStatus().name();
        this.tag = todo.getTag();
        this.createdAt = todo.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.createdBy = SimpleUserDto.fromEntity(todo.getCreatedBy());
        this.assignedTo = (todo.getAssignedTo() != null) ? SimpleUserDto.fromEntity(todo.getAssignedTo()) : null;
    }
}
