/*
package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.TodoRequestDto;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/*

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody TodoRequestDto dto,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(todoService.createTodo(dto, username));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Todo todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody TodoRequestDto dto) {
        Todo updated = todoService.updateTodo(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTodoSafely(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


}
*/
/*


@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 사용자의 할 일 목록을 조회
    @GetMapping
    public ResponseEntity<List<Todo>> getMyTodos(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(todoService.getMyTodos(username));
    }

    // 할 일을 생성
    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody TodoRequestDto dto,
                                           @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(todoService.createTodo(dto, username));
    }

    // ID로 할 일을 조회 (작성자 본인만 가능)
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id,
                                            @AuthenticationPrincipal String username) {
        Todo todo = todoService.getTodoById(id, username);
        return ResponseEntity.ok(todo);
    }

    // 할 일을 수정 (작성자 본인만 가능)
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id,
                                           @RequestBody TodoRequestDto dto,
                                           @AuthenticationPrincipal String username) {
        Todo updated = todoService.updateTodo(id, dto, username);
        return ResponseEntity.ok(updated);
    }

    // 할 일을 삭제 (작성자 본인만 가능)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTodoSafely(@PathVariable Long id,
                                                 @AuthenticationPrincipal String username) {
        todoService.deleteTodo(id, username);
        return ResponseEntity.noContent().build();
    }
}*/

package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.PersonalDashboardResponseDto;
import com.todotogether.todo_backend.dto.TodoRequestDto;
import com.todotogether.todo_backend.dto.TodoResponseDto;
import com.todotogether.todo_backend.dto.TodoStatusUpdateRequestDto;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.entity.TodoStatus;
import com.todotogether.todo_backend.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 사용자의 할 일 목록을 조회
    // 모든 코드 조회
    /*@GetMapping
    public ResponseEntity<List<TodoResponseDto>> getMyTodos(@AuthenticationPrincipal String username) {
        List<Todo> todos = todoService.getMyTodos(username);
        List<TodoResponseDto> response = todos.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }*/
    // 모든 할 일 조회, status, tag로 필터링 기능 추가
    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getMyTodos(
            @AuthenticationPrincipal String username,
            @RequestParam(required = false) TodoStatus status,
            @RequestParam(required = false) String tag
    ) {
        List<Todo> todos = todoService.getMyTodosFiltered(username, status, tag);
        List<TodoResponseDto> response = todos.stream()
                .map(TodoResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }


    // 할 일을 생성
    @PostMapping
    public ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoRequestDto dto,
                                                      @AuthenticationPrincipal String username) {
        Todo created = todoService.createTodo(dto, username);
        return ResponseEntity.ok(new TodoResponseDto(created));
    }

    // ID로 할 일을 조회 (작성자 본인만 가능)
    @GetMapping("/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(@PathVariable Long id,
                                                       @AuthenticationPrincipal String username) {
        Todo todo = todoService.getTodoById(id, username);
        return ResponseEntity.ok(new TodoResponseDto(todo));
    }

    // 할 일을 수정 (작성자 본인만 가능)
    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(@PathVariable Long id,
                                                      @RequestBody TodoRequestDto dto,
                                                      @AuthenticationPrincipal String username) {
        Todo updated = todoService.updateTodo(id, dto, username);
        return ResponseEntity.ok(new TodoResponseDto(updated));
    }

    // 할 일을 삭제 (작성자 본인만 가능)
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteTodoSafely(@PathVariable Long id,
                                                 @AuthenticationPrincipal String username) {
        todoService.deleteTodo(id, username);
        return ResponseEntity.noContent().build();
    }
    // 할 일 status 변경
    @PatchMapping("/{id}/status")
    public ResponseEntity<TodoResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody TodoStatusUpdateRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Todo updated = todoService.updateTodoStatus(id, dto.getStatus(), userDetails.getUsername());
        return ResponseEntity.ok(new TodoResponseDto(updated));
    }

    // 특정 그룹의 할 일 목록을 조회
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<TodoResponseDto>> getTodosByGroup(@PathVariable Long groupId,
                                                                 @AuthenticationPrincipal String username) {
        List<Todo> todos = todoService.getTodosByGroup(groupId, username);
        List<TodoResponseDto> response = todos.stream()
                .map(TodoResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/personal")
    public ResponseEntity<List<TodoResponseDto>> getPersonalTodos(
            @AuthenticationPrincipal String username
    ) {
        List<Todo> todos = todoService.getMyPersonalTodos(username);
        List<TodoResponseDto> response = todos.stream()
                .map(TodoResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/dashboard")
    public ResponseEntity<PersonalDashboardResponseDto> getPersonalDashboard(
            @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(todoService.getPersonalDashboard(username));
    }

}
