package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardResponseDto {
    private String groupTitle;
    private long totalTodos;
    private Map<TodoStatus, Long> statusCounts;
    private double completionRate;
    private Map<String, Long> todosPerAssignee; // username -> 할 일 수
    private Map<String, Long> tagCounts;        // tag -> count
}
