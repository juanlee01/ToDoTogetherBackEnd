package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.TodoStatus;
import lombok.Getter;

@Getter
public class TodoStatusUpdateRequestDto {
    private TodoStatus status;
}
