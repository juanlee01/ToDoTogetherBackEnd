package com.todotogether.todo_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeDto {
    private String oldPassword;
    private String newPassword;
}
