package com.todotogether.todo_backend.dto;

import lombok.Getter;

@Getter
public class UserProfileUpdateDto {
    private String email;
    // 필요 시 password 등 추가
}
