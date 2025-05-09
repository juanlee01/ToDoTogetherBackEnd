package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.entity.UsersRole;
import com.todotogether.todo_backend.entity.GroupMember;
import com.todotogether.todo_backend.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupPermissionService {

    private final GroupMemberRepository groupMemberRepository;

    /**
     * 그룹 내 사용자 권한 조회
     */
    public UsersRole getRoleInGroup(Long groupId, Long userId) {
        GroupMember member = groupMemberRepository.findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new RuntimeException("그룹에 속한 멤버가 아닙니다."));
        return member.getRole();
    }

    /**
     * 특정 역할 이상인지 확인
     */
    public boolean hasAtLeastRole(Long groupId, Long userId, UsersRole requiredRole) {
        UsersRole actualRole = getRoleInGroup(groupId, userId);
        return actualRole.ordinal() <= requiredRole.ordinal(); // LEADER(0) < MEMBER(1) < GUEST(2)
    }

    /**
     * 본인 또는 리더 여부 확인
     */
    public boolean isSelfOrLeader(Long groupId, Long currentUserId, Long resourceOwnerId) {
        if (currentUserId.equals(resourceOwnerId)) return true;
        return getRoleInGroup(groupId, currentUserId) == UsersRole.LEADER;
    }
}
