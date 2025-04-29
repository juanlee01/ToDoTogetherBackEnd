package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.GroupRequestDto;
import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.repository.GroupRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public Group createGroup(GroupRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Group group = new Group();
        group.setTitle(dto.getTitle());
        group.setCreatedUser(user);
        group.setCreatedAt(LocalDateTime.now());

        return groupRepository.save(group);
    }
}
