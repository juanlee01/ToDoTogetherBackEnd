package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.entity.*;
import com.todotogether.todo_backend.repository.GroupMemberRepository;
import com.todotogether.todo_backend.repository.TodoRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoPermissionService {

    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    /**
     * 할 일 작성 권한: GUEST는 작성 불가
     */
    public void validateTodoCreation(Long groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, user.getId())
                .orElseThrow(() -> new RuntimeException("그룹 멤버가 아닙니다."));

        if (member.getRole() == UsersRole.GUEST) {
            throw new RuntimeException("GUEST는 할 일을 작성할 수 없습니다.");
        }
    }

    /**
     * 수정/삭제 권한 확인 (LEADER는 모두 가능, MEMBER는 본인만)
     */
    public void validateTodoModification(Todo todo, String username) {
        User user = todo.getCreatedBy();
        if (!todo.getCreatedBy().getUsername().equals(username)) {
            throw new RuntimeException("작성자만 수정 또는 삭제할 수 있습니다.");
        }
    }
}
