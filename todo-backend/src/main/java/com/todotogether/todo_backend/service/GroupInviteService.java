package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.entity.*;
import com.todotogether.todo_backend.repository.GroupInviteRepository;
import com.todotogether.todo_backend.repository .GroupRepository;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupInviteService {

    private final GroupInviteRepository groupInviteRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    // 초대 보내기 (이메일 기반)
    public void inviteUserToGroup(Long groupId, String senderUsername, String receiverEmail) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("그룹을 찾을 수 없습니다."));
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("보낸 사람을 찾을 수 없습니다."));
        User receiver = userRepository.findByEmail(receiverEmail)
                .orElseThrow(() -> new RuntimeException("받는 사람을 찾을 수 없습니다."));

        groupInviteRepository.findByGroupAndReceiver(group, receiver)
                .ifPresent(invite -> {
                    throw new RuntimeException("이미 초대된 사용자입니다.");
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
    public void acceptInvite(Long inviteId, String username) {
        GroupInvite invite = groupInviteRepository.findById(inviteId)
                .orElseThrow(() -> new RuntimeException("초대를 찾을 수 없습니다."));

        if (!invite.getReceiver().getUsername().equals(username)) {
            throw new RuntimeException("본인만 초대를 수락할 수 있습니다.");
        }

        invite.setStatus(InviteStatus.ACCEPTED);
        groupInviteRepository.save(invite);

        // 그룹에 멤버 추가 (GroupMembers 테이블에 직접 추가해야 함)
        // TODO: 나중에 GroupMember 엔티티 연동할 때 추가 작업
    }

    // 초대 거절
    public void rejectInvite(Long inviteId, String username) {
        GroupInvite invite = groupInviteRepository.findById(inviteId)
                .orElseThrow(() -> new RuntimeException("초대를 찾을 수 없습니다."));

        if (!invite.getReceiver().getUsername().equals(username)) {
            throw new RuntimeException("본인만 초대를 거절할 수 있습니다.");
        }

        invite.setStatus(InviteStatus.REJECTED);
        groupInviteRepository.save(invite);
    }

    // 받은 초대 목록 조회
    public List<GroupInvite> getMyPendingInvites(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        return groupInviteRepository.findAllByReceiverAndStatus(user, InviteStatus.PENDING);
    }
}
