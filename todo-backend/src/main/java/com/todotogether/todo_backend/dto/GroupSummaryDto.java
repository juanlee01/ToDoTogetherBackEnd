package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.entity.UsersRole;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GroupSummaryDto {
    private final Long groupId;
    private final String title;
    private final LocalDateTime createdAt;
    private final UsersRole myRole;

    public GroupSummaryDto(Group group, UsersRole myRole) {
        this.groupId = group.getId();
        this.title = group.getTitle();
        this.createdAt = group.getCreatedAt();
        this.myRole = myRole;
    }
}
