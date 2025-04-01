package com.todotogether.todo_backend.repository;

import  com.todotogether.todo_backend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 기본적인 findAll(), findById() 등은 자동으로 제공됨
}