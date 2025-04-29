package com.todotogether.todo_backend.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super("해당 Todo가 존재하지 않습니다.");
    }
}