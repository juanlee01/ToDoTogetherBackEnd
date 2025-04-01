package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.TodoRequestDto;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.repository.TodoRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo createTodo(TodoRequestDto dto) {
        User creator = userRepository.findById(dto.getCreatedId())
                .orElseThrow(() -> new RuntimeException("작성자 없음"));

        User assignee = null;
        if (dto.getAssignedTo() != null) {
            assignee = userRepository.findById(dto.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("담당자 없음"));
        }

        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setCreatedBy(creator);
        todo.setAssignedTo(assignee);
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());
        todo.setCreatedAt(LocalDateTime.now());

        return todoRepository.save(todo);
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 Todo 없음"));
    }

    public Todo updateTodo(Long id, TodoRequestDto dto) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 Todo 없음"));

        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());

        if (dto.getAssignedTo() != null) {
            User assignee = userRepository.findById(dto.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("담당자 없음"));
            todo.setAssignedTo(assignee);
        }

        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 Todo 없음"));
        todoRepository.delete(todo);
    }


}
