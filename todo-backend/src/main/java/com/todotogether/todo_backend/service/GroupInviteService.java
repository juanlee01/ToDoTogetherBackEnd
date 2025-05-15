//package com.todotogether.todo_backend.service;
//
//import com.todotogether.todo_backend.entity.*;
//import com.todotogether.todo_backend.repository.GroupInviteRepository;
//import com.todotogether.todo_backend.repository.GroupMemberRepository;
//import com.todotogether.todo_backend.repository .GroupRepository;
//import com.todotogether.todo_backend.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class GroupInviteService {
//
//    private final GroupInviteRepository groupInviteRepository;
//    private final GroupRepository groupRepository;
//    private final UserRepository userRepository;
//    private final GroupMemberRepository groupMemberRepository;
//
//
//    // 초대 보내기 (이메일 기반)
//    public void inviteUserToGroup(Long groupId, String senderUsername, String receiverEmail) {
//        Group group = groupRepository.findById(groupId)
//                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
//        User sender = userRepository.findByUsername(senderUsername)
//                .orElseThrow(() -> new RuntimeException("보낸 사람을 찾을 수 없습니다."));
//        User receiver = userRepository.findByEmail(receiverEmail)
//                .orElseThrow(() -> new RuntimeException("받는 사람을 찾을 수 없습니다."));
//
//        groupInviteRepository.findByGroupAndReceiver(group, receiver)
//                .ifPresent(invite -> {
//                    throw new RuntimeException("이미 초대된 사용자입니다.");
//                });
//
//        GroupInvite invite = new GroupInvite();
//        invite.setGroup(group);
//        invite.setSender(sender);
//        invite.setReceiver(receiver);
//        invite.setStatus(InviteStatus.PENDING);
//        invite.setCreatedAt(LocalDateTime.now());
//
//        groupInviteRepository.save(invite);
//    }
//
//
//    // 초대 수락
//    public void acceptInvite(Long inviteId, String username) {
//        GroupInvite invite = groupInviteRepository.findById(inviteId)
//                .orElseThrow(() -> new RuntimeException("초대를 찾을 수 없습니다."));
//
//        if (!invite.getReceiver().getUsername().equals(username)) {
//            throw new RuntimeException("본인만 초대를 수락할 수 있습니다.");
//        }
//
//        invite.setStatus(InviteStatus.ACCEPTED);
//        groupInviteRepository.save(invite);
//
//        // 그룹에 멤버 추가 (GroupMembers 테이블에 직접 추가해야 함)
//
//        // 초대 상태 업데이트
//        invite.setStatus(InviteStatus.ACCEPTED);
//        groupInviteRepository.save(invite);
//
//        // 이미 가입된 멤버인지 확인
//        Group group = invite.getGroup();
//        User receiver = invite.getReceiver();
//        GroupMemberId memberId = new GroupMemberId(group.getId(), receiver.getId());
//
//        boolean isAlreadyMember = groupMemberRepository.existsById(memberId);
//        if (isAlreadyMember) {
//            return; // 이미 멤버라면 추가하지 않음
//        }
//
//        // 새 그룹 멤버 추가
//        GroupMember newMember = new GroupMember();
//        newMember.setId(memberId);
//        newMember.setGroup(group);
//        newMember.setUser(receiver);
//        newMember.setRole(UsersRole.MEMBER);  // 기본 역할
//        newMember.setJoinedAt(LocalDateTime.now());
//
//        groupMemberRepository.save(newMember);
//    }
//
//    // 초대 거절
//    public void rejectInvite(Long inviteId, String username) {
//        GroupInvite invite = groupInviteRepository.findById(inviteId)
//                .orElseThrow(() -> new RuntimeException("초대를 찾을 수 없습니다."));
//
//        if (!invite.getReceiver().getUsername().equals(username)) {
//            throw new RuntimeException("본인만 초대를 거절할 수 있습니다.");
//        }
//
//        invite.setStatus(InviteStatus.REJECTED);
//        groupInviteRepository.save(invite);
//    }
//
//    // 받은 초대 목록 조회
//    public List<GroupInvite> getMyPendingInvites(String username) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
//
//        return groupInviteRepository.findAllByReceiverAndStatus(user, InviteStatus.PENDING);
//    }
//}

package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.entity.*;
import com.todotogether.todo_backend.repository.GroupInviteRepository;
import com.todotogether.todo_backend.repository.GroupMemberRepository;
import com.todotogether.todo_backend.repository.GroupRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupInviteService {

    private final GroupInviteRepository groupInviteRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    public void inviteUserToGroup(Long groupId, String senderUsername, String receiverEmail) {
        if (senderUsername.equals(receiverEmail)) {
            throw new IllegalArgumentException("본인을 초대할 수 없습니다.");
        }

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("그룹을 찾을 수 없습니다."));

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new IllegalArgumentException("초대 발신자를 찾을 수 없습니다."));

        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일에 해당하는 사용자가 존재하지 않습니다."));

        GroupMemberId memberId = new GroupMemberId(group.getId(), receiver.getId());
        if (groupMemberRepository.existsById(memberId)) {
            throw new IllegalStateException("해당 사용자는 이미 그룹에 속해 있습니다.");
        }

        // ✅ PENDING 상태인 초대만 중복으로 간주
        groupInviteRepository.findByGroupAndReceiverAndStatus(group, receiver, InviteStatus.PENDING)
                .ifPresent(invite -> {
                    throw new IllegalStateException("이미 초대가 전송된 사용자입니다.");
                });

        GroupInvite invite = new GroupInvite();
        invite.setGroup(group);
        invite.setSender(sender);
        invite.setReceiver(receiver);
        invite.setStatus(InviteStatus.PENDING);
        invite.setCreatedAt(LocalDateTime.now());

        groupInviteRepository.save(invite);
    }



    // 초대 수락
    @Transactional
    public void acceptInvite(Long inviteId, String username) {
        GroupInvite invite = groupInviteRepository.findById(inviteId)
                .orElseThrow(() -> new IllegalArgumentException("초대를 찾을 수 없습니다."));

        if (!invite.getReceiver().getUsername().equals(username)) {
            throw new SecurityException("본인만 초대를 수락할 수 있습니다.");
        }

        invite.setStatus(InviteStatus.ACCEPTED);
        groupInviteRepository.save(invite);

        Group group = invite.getGroup();
        User receiver = invite.getReceiver();
        GroupMemberId memberId = new GroupMemberId(group.getId(), receiver.getId());

        if (groupMemberRepository.existsById(memberId)) {
            return; // 이미 멤버면 추가하지 않음
        }

        GroupMember newMember = new GroupMember();
        newMember.setId(memberId);
        newMember.setGroup(group);
        newMember.setUser(receiver);
        newMember.setRole(UsersRole.MEMBER);
        newMember.setJoinedAt(LocalDateTime.now());

        groupMemberRepository.save(newMember);
    }

    // 초대 거절
    @Transactional
    public void rejectInvite(Long inviteId, String username) {
        GroupInvite invite = groupInviteRepository.findById(inviteId)
                .orElseThrow(() -> new IllegalArgumentException("초대를 찾을 수 없습니다."));

        if (!invite.getReceiver().getUsername().equals(username)) {
            throw new SecurityException("본인만 초대를 거절할 수 있습니다.");
        }

        invite.setStatus(InviteStatus.REJECTED);
        groupInviteRepository.save(invite);
    }

    // 대기 중 초대 목록 조회
    public List<GroupInvite> getMyPendingInvites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return groupInviteRepository.findAllByReceiverAndStatus(user, InviteStatus.PENDING);
    }
}
