package com.todotogether.todo_backend.repository;

import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.entity.GroupMember;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.entity.GroupMemberId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, GroupMemberId> {
    boolean existsByGroupAndUser(Group group, User user);
    List<GroupMember> findAllByGroup(Group group);
    Optional<GroupMember> findByGroupIdAndUserId(Long groupId, Long userId);

}
