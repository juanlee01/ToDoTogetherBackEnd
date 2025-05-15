package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class UserProfileResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    private final LocalDateTime createdAt;

    public UserProfileResponseDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.createdAt = user.getCreatedAt();
    }
}
