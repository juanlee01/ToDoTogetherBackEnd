package com.todotogether.todo_backend.repository;

import com.todotogether.todo_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // 기본 CRUD 메서드 자동 제공됨 (findById, save 등)
}
