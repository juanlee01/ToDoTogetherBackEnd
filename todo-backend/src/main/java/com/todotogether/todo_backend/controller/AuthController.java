package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.LoginRequestDto;
import com.todotogether.todo_backend.dto.LoginResponseDto;
import com.todotogether.todo_backend.dto.SignupRequestDto;
import com.todotogether.todo_backend.dto.UserResponseDto;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody SignupRequestDto dto) {
        User newUser = userService.signup(dto);
        return ResponseEntity.ok(new UserResponseDto(newUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        LoginResponseDto response = userService.login(dto);
        return ResponseEntity.ok(response);
    }

}
