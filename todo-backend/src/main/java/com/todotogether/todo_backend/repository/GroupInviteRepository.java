package com.todotogether.todo_backend.repository;

import com.todotogether.todo_backend.entity.GroupInvite;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.entity.InviteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupInviteRepository extends JpaRepository<GroupInvite, Long> {

    // 내가 받은 초대 목록 (PENDING)
    List<GroupInvite> findAllByReceiverAndStatus(User receiver, InviteStatus status);

    // 특정 그룹에 특정 유저가 이미 초대된 내역이 있는지 확인
    Optional<GroupInvite> findByGroupAndReceiver(Group group, User receiver);

    // PENDING 상태의 초대 여부 확인
    Optional<GroupInvite> findByGroupAndReceiverAndStatus(Group group, User receiver, InviteStatus status);

}

