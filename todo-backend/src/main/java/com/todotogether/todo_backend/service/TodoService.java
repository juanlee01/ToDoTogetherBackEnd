/*
package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.TodoRequestDto;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.repository.TodoRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
*/
/*
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

    public Todo createTodo(TodoRequestDto dto, String username) {
        User creator = userRepository.findByUsername(username)
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


}*/
/*


@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    // 사용자의 할 일 목록을 조회
    public List<Todo> getMyTodos(String username) {
        User user = getUserByUsername(username);
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getCreatedBy().equals(user))
                .toList();
    }

    // 할 일을 생성
    public Todo createTodo(TodoRequestDto dto, String username) {
        User creator = getUserByUsername(username);
        User assignee = getUserByIdNullable(dto.getAssignedTo());

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

    // ID로 할 일을 조회 (작성자 본인만 가능)
    public Todo getTodoById(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        return todo;
    }

    // 할 일을 수정 (작성자 본인만 가능)
    public Todo updateTodo(Long id, TodoRequestDto dto, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);

        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());

        User assignee = getUserByIdNullable(dto.getAssignedTo());
        todo.setAssignedTo(assignee);

        return todoRepository.save(todo);
    }

    // 할 일을 삭제 (작성자 본인만 가능)
    public void deleteTodo(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        todoRepository.delete(todo);
    }

    // 사용자 이름으로 사용자 조회
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    // ID로 사용자 조회 (nullable)
    private User getUserByIdNullable(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("담당자 없음"));
    }

    // ID로 할 일을 조회
    private Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 Todo 없음"));
    }

    // 작성자 검증
    private void validateOwner(Todo todo, String username) {
        if (!todo.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("작성자 본인만 접근 가능합니다.");
        }
    }
}

*/


/*package com.todotogether.todo_backend.service;

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

    // 사용자의 할 일 목록을 조회
    public List<Todo> getMyTodos(String username) {
        User user = getUserByUsername(username);
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getCreatedBy().equals(user))
                .toList();
    }

    // 할 일을 생성
    public Todo createTodo(TodoRequestDto dto, String username) {
        User creator = getUserByUsername(username);
        User assignee = getUserByIdNullable(dto.getAssignedTo());

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

    // ID로 할 일을 조회 (작성자 본인만 가능)
    public Todo getTodoById(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        return todo;
    }

    // 할 일을 수정 (작성자 본인만 가능)
    public Todo updateTodo(Long id, TodoRequestDto dto, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);

        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());

        User assignee = getUserByIdNullable(dto.getAssignedTo());
        todo.setAssignedTo(assignee);

        return todoRepository.save(todo);
    }

    // 할 일을 삭제 (작성자 본인만 가능)
    public void deleteTodo(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        todoRepository.delete(todo);
    }

    // 사용자 이름으로 사용자 조회
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));
    }

    // ID로 사용자 조회 (nullable)
    private User getUserByIdNullable(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("담당자 없음"));
    }

    // ID로 할 일을 조회
    private Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 Todo 없음"));
    }

    // 작성자 검증
    private void validateOwner(Todo todo, String username) {
        if (!todo.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("작성자 본인만 접근 가능합니다.");
        }
    }
}*/

package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.TodoRequestDto;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.entity.TodoStatus;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.exception.TodoNotFoundException;
import com.todotogether.todo_backend.exception.UnauthorizedException;
import com.todotogether.todo_backend.exception.UserNotFoundException;
import com.todotogether.todo_backend.repository.GroupMemberRepository;
import com.todotogether.todo_backend.repository.GroupRepository;
import com.todotogether.todo_backend.repository.TodoRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository, GroupRepository groupRepository, GroupMemberRepository groupMemberRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    public List<Todo> getMyTodos(String username) {
        User user = getUserByUsername(username);
        return todoRepository.findAll().stream()
                .filter(todo -> todo.getCreatedBy().equals(user))
                .toList();
    }

    public Todo createTodo(TodoRequestDto dto, String username) {
        User creator = getUserByUsername(username);
        User assignee = getUserByIdNullable(dto.getAssignedTo());

        Todo todo = new Todo();
        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setCreatedBy(creator);
        todo.setAssignedTo(assignee);
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());
        todo.setCreatedAt(LocalDateTime.now());
        if (dto.getGroupId() != null) {
            Group group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            boolean isMember = groupMemberRepository.existsByGroupAndUser(group, user);
            if (!isMember) throw new RuntimeException("그룹 멤버만 그룹 Todo를 만들 수 있습니다.");

            todo.setGroup(group);
        }

        return todoRepository.save(todo);
    }

    public Todo getTodoById(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        return todo;
    }

    public Todo updateTodo(Long id, TodoRequestDto dto, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);

        todo.setTitle(dto.getTitle());
        todo.setBody(dto.getBody());
        todo.setStatus(dto.getStatus());
        todo.setTag(dto.getTag());

        User assignee = getUserByIdNullable(dto.getAssignedTo());
        todo.setAssignedTo(assignee);

        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username);
        todoRepository.delete(todo);
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    private User getUserByIdNullable(Long id) {
        if (id == null) return null;
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    private Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(TodoNotFoundException::new);
    }

    private void validateOwner(Todo todo, String username) {
        if (!todo.getCreatedBy().getUsername().equals(username)) {
            throw new UnauthorizedException("작성자 본인만 접근 가능합니다.");
        }
    }

    public Todo updateTodoStatus(Long id, TodoStatus status, String username) {
        Todo todo = getTodoById(id);
        validateOwner(todo, username); // 작성자인지 확인

        todo.setStatus(status);
        return todoRepository.save(todo);
    }

    public List<Todo> getMyTodosFiltered(String username, TodoStatus status, String tag) {
        User user = getUserByUsername(username);

        return todoRepository.findAll().stream()
                .filter(todo -> todo.getCreatedBy().equals(user))
                .filter(todo -> status == null || todo.getStatus() == status)
                .filter(todo -> tag == null || tag.equals(todo.getTag()))
                .toList();
    }

    public List<Todo> getTodosByGroup(Long groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        boolean isMember = groupMemberRepository.existsByGroupAndUser(group, user);
        if (!isMember) throw new RuntimeException("그룹 멤버만 접근할 수 있습니다.");

        return todoRepository.findAllByGroup(group);
    }



}