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


    public Group createGroup(GroupRequestDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 1. 그룹 생성
        Group group = new Group();
        group.setTitle(dto.getTitle());
        group.setCreatedUser(user);
        group.setCreatedAt(LocalDateTime.now());

        Group savedGroup = groupRepository.save(group);

        // 2. 생성자를 그룹 멤버로 등록
        GroupMemberId memberId = new GroupMemberId(savedGroup.getId(), user.getId());

        GroupMember creatorMember = new GroupMember();
        creatorMember.setId(memberId);
        creatorMember.setGroup(savedGroup);
        creatorMember.setUser(user);
        creatorMember.setRole(UsersRole.LEADER); // 생성자는 관리자 역할
        creatorMember.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(creatorMember);

        return savedGroup;
    }


    public List<GroupMember> getGroupMembers(Long groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        // 요청자가 그룹의 멤버인지 확인
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청한 사용자를 찾을 수 없습니다."));

        boolean isMember = groupMemberRepository.findAllByGroup(group).stream()
                .anyMatch(member -> member.getUser().getId().equals(requester.getId()));
        if (!isMember) {
            throw new RuntimeException("해당 그룹의 멤버가 아닙니다.");
        }

        return groupMemberRepository.findAllByGroup(group);
    }

    public List<GroupMemberResponseDto> getGroupMemberDtos(Long groupId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        // 요청자가 해당 그룹의 멤버인지 확인
        boolean isMember = groupMemberRepository.findAllByGroup(group).stream()
                .anyMatch(member -> member.getUser().getUsername().equals(requesterUsername));
        if (!isMember) {
            throw new RuntimeException("해당 그룹에 대한 접근 권한이 없습니다.");
        }

        return groupMemberRepository.findAllByGroup(group).stream()
                .map(GroupMemberResponseDto::new)
                .toList();
    }

    @Transactional
    public void leaveGroup(Long groupId, String username) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 그룹 생성자는 탈퇴할 수 없음 (원하면 제거 가능)
        if (group.getCreatedUser().equals(user)) {
            throw new RuntimeException("그룹 생성자는 탈퇴할 수 없습니다.");
        }

        GroupMemberId memberId = new GroupMemberId(group.getId(), user.getId());

        if (!groupMemberRepository.existsById(memberId)) {
            throw new RuntimeException("해당 그룹에 가입된 사용자가 아닙니다.");
        }

        groupMemberRepository.deleteById(memberId);
    }

//    @Transactional
//    public void changeMemberRole(Long groupId, Long targetUserId, String requesterUsername, UsersRole newRole) {
//        User requester = userRepository.findByUsername(requesterUsername)
//                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));
//
//        // 요청자가 그룹의 관리자여야 함
//        GroupMember requesterMember = groupMemberRepository.findByGroupIdAndUserId(groupId, requester.getId())
//                .orElseThrow(() -> new RuntimeException("요청자는 그룹의 멤버가 아닙니다."));
//        if (requesterMember.getRole() != UsersRole.LEADER) {
//            throw new RuntimeException("권한이 없습니다.");
//        }
//
//        // 대상 멤버 조회
//        GroupMember targetMember = groupMemberRepository.findByGroupIdAndUserId(groupId, targetUserId)
//                .orElseThrow(() -> new RuntimeException("대상 멤버를 찾을 수 없습니다."));
//
//        // 자기 자신은 역할 변경 불가
//        if (requester.getId().equals(targetUserId)) {
//            throw new RuntimeException("자기 자신의 역할은 변경할 수 없습니다.");
//        }
//
//        targetMember.setRole(newRole);
//    }

    @Transactional
    public void transferLeadership(Long groupId, Long newLeaderUserId, String requesterUsername) {
        // 1. 요청자 검증
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        GroupMember requesterMember = groupMemberRepository.findByGroupIdAndUserId(groupId, requester.getId())
                .orElseThrow(() -> new RuntimeException("요청자는 그룹 멤버가 아닙니다."));

        if (requesterMember.getRole() != UsersRole.LEADER) {
            throw new RuntimeException("LEADER만 리더를 위임할 수 있습니다.");
        }

        // 2. 새로운 리더 대상 검증
        GroupMember targetMember = groupMemberRepository.findByGroupIdAndUserId(groupId, newLeaderUserId)
                .orElseThrow(() -> new RuntimeException("대상 사용자는 그룹 멤버가 아닙니다."));

        if (targetMember.getRole() != UsersRole.MEMBER) {
            throw new RuntimeException("LEADER는 MEMBER에게만 위임할 수 있습니다.");
        }

        // 3. 리더 변경
        targetMember.setRole(UsersRole.LEADER);
        requesterMember.setRole(UsersRole.MEMBER); // 자동 강등

        // 그룹 엔티티에도 리더 변경 정보 반영
        User newLeader = userRepository.findById(newLeaderUserId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        group.setCreatedUser(newLeader);
    }

    @Transactional
    public void changeMemberAccessLevel(Long groupId, Long targetUserId, String requesterUsername, UsersRole newRole) {
        // 요청자 검증
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("요청자를 찾을 수 없습니다."));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));

        GroupMember requesterMember = groupMemberRepository.findByGroupIdAndUserId(groupId, requester.getId())
                .orElseThrow(() -> new RuntimeException("요청자는 그룹 멤버가 아닙니다."));

        if (requesterMember.getRole() != UsersRole.LEADER) {
            throw new RuntimeException("LEADER만 권한을 변경할 수 있습니다.");
        }

        // 대상 멤버 조회
        GroupMember targetMember = groupMemberRepository.findByGroupIdAndUserId(groupId, targetUserId)
                .orElseThrow(() -> new RuntimeException("대상 사용자는 그룹 멤버가 아닙니다."));

        // LEADER는 변경 대상이 될 수 없음
        if (targetMember.getRole() == UsersRole.LEADER) {
            throw new RuntimeException("LEADER의 권한은 변경할 수 없습니다.");
        }

        // MEMBER <-> GUEST 만 허용
        if (newRole != UsersRole.MEMBER && newRole != UsersRole.GUEST) {
            throw new RuntimeException("변경 가능한 역할은 MEMBER 또는 GUEST만 가능합니다.");
        }

        targetMember.setRole(newRole);
    }


}
