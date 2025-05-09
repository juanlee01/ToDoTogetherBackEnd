package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.TodoStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TodoRequestDto {
    private String title;
    private String body;
    //private Long createdId;
    private Long assignedTo;
    private TodoStatus status;
    private String tag;
    private Long groupId; // 선택적으로 할당 가능
}

