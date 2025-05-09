package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.GroupMemberResponseDto;
import com.todotogether.todo_backend.dto.GroupRequestDto;
import com.todotogether.todo_backend.entity.*;
import com.todotogether.todo_backend.repository.GroupRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import com.todotogether.todo_backend.repository.GroupMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final GroupPermissionService permissionService;

    /**
     * 그룹을 생성하고 생성자를 LEADER로 등록한다.
     */
    public Group createGroup(GroupRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Group group = new Group();
        group.setTitle(dto.getTitle());
        group.setCreatedUser(user);
        group.setCreatedAt(LocalDateTime.now());

        Group savedGroup = groupRepository.save(group);

        GroupMember leader = new GroupMember();
        leader.setId(new GroupMemberId(savedGroup.getId(), user.getId()));
        leader.setGroup(savedGroup);
        leader.setUser(user);
        leader.setRole(UsersRole.LEADER);
        leader.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(leader);
        return savedGroup;
    }

    /**
     * 그룹의 모든 멤버를 조회한다 (접근자는 멤버여야 함).
     */
    public List<GroupMemberResponseDto> getGroupMemberDtos(Long groupId, String requesterUsername) {
        // 요청자 정보 조회
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));

        // 요청자의 그룹 내 권한 확인
        permissionService.getRoleInGroup(groupId, requester.getId());

        // 그룹 정보 조회
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        // 그룹 멤버 조회 및 DTO 변환
        return groupMemberRepository.findAllByGroup(group).stream()
                .map(GroupMemberResponseDto::new)
                .toList();
    }


    /**
     * 그룹에서 사용자가 탈퇴한다. (LEADER는 탈퇴 불가)
     */
    @Transactional
    public void leaveGroup(Long groupId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        if (group.getCreatedUser().equals(user)) {
            throw new RuntimeException("LEADER는 탈퇴할 수 없습니다.");
        }

        GroupMemberId memberId = new GroupMemberId(group.getId(), user.getId());

        if (!groupMemberRepository.existsById(memberId)) {
            throw new RuntimeException("그룹에 소속되어 있지 않습니다.");
        }

        groupMemberRepository.deleteById(memberId);
    }

    /**
     * LEADER가 본인의 리더 역할을 다른 MEMBER에게 위임한다.
     */
    @Transactional
    public void transferLeadership(Long groupId, Long newLeaderUserId, String requesterUsername) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));

        UsersRole requesterRole = permissionService.getRoleInGroup(groupId, requester.getId());
        if (requesterRole != UsersRole.LEADER) {
            throw new RuntimeException("LEADER만 리더를 위임할 수 있습니다.");
        }

        GroupMember targetMember = groupMemberRepository.findByGroupIdAndUserId(groupId, newLeaderUserId)
                .orElseThrow(() -> new RuntimeException("대상 사용자는 그룹 멤버가 아닙니다."));

        if (targetMember.getRole() != UsersRole.MEMBER) {
            throw new RuntimeException("리더는 MEMBER에게만 위임할 수 있습니다.");
        }

        GroupMember currentLeader = groupMemberRepository.findByGroupIdAndUserId(groupId, requester.getId()).get();
        currentLeader.setRole(UsersRole.MEMBER);
        targetMember.setRole(UsersRole.LEADER);

        Group group = groupRepository.findById(groupId).orElseThrow();
        group.setCreatedUser(targetMember.getUser());
    }

    /**
     * LEADER가 특정 멤버의 역할을 MEMBER 또는 GUEST로 변경한다.
     */
    @Transactional
    public void changeMemberAccessLevel(Long groupId, Long targetUserId, String requesterUsername, UsersRole newRole) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));

        UsersRole requesterRole = permissionService.getRoleInGroup(groupId, requester.getId());
        if (requesterRole != UsersRole.LEADER) {
            throw new RuntimeException("LEADER만 역할 변경이 가능합니다.");
        }

        GroupMember targetMember = groupMemberRepository.findByGroupIdAndUserId(groupId, targetUserId)
                .orElseThrow(() -> new RuntimeException("대상 멤버가 존재하지 않습니다."));

        if (targetMember.getRole() == UsersRole.LEADER) {
            throw new RuntimeException("LEADER는 변경 대상이 될 수 없습니다.");
        }

        if (newRole != UsersRole.MEMBER && newRole != UsersRole.GUEST) {
            throw new RuntimeException("변경 가능한 역할은 MEMBER 또는 GUEST입니다.");
        }

        targetMember.setRole(newRole);
    }
}
