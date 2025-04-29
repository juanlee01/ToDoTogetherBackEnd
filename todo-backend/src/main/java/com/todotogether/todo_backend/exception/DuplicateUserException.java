package com.todotogether.todo_backend.exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException() {
        super("이미 존재하는 사용자입니다.");
    }
}