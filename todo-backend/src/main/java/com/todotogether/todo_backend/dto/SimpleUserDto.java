package com.todotogether.todo_backend.dto;

import com.todotogether.todo_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleUserDto {
    private Long id;
    private String username;

    public static SimpleUserDto fromEntity(User user) {
        return new SimpleUserDto(user.getId(), user.getUsername());
    }
}
