package com.todotogether.todo_backend.repository;

import com.todotogether.todo_backend.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}
