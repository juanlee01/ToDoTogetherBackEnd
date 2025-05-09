package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.GroupMember;
import com.todotogether.todo_backend.entity.UsersRole;
import lombok.Getter;

@Getter
public class GroupMemberResponseDto {
    private final Long userId;
    private final String username;
    private final String email;
    private final UsersRole role;

    public GroupMemberResponseDto(GroupMember member) {
        this.userId = member.getUser().getId();
        this.username = member.getUser().getUsername();
        this.email = member.getUser().getEmail();
        this.role = member.getRole();
    }
}
