
package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.GroupMemberResponseDto;
import com.todotogether.todo_backend.dto.GroupRequestDto;
import com.todotogether.todo_backend.dto.GroupResponseDto;
import com.todotogether.todo_backend.dto.TodoResponseDto;
import com.todotogether.todo_backend.entity.Group;
import com.todotogether.todo_backend.entity.GroupInvite;
import com.todotogether.todo_backend.entity.GroupMember;
import com.todotogether.todo_backend.entity.Todo;
import com.todotogether.todo_backend.service.GroupInviteService;
import com.todotogether.todo_backend.service.GroupService;
import com.todotogether.todo_backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupInviteService groupInviteService;
    private final TodoService todoService;

    // 🔹 1. 그룹 생성
    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupRequestDto dto,
                                                        @AuthenticationPrincipal String username) {
        Group group = groupService.createGroup(dto, username);
        return ResponseEntity.ok(new GroupResponseDto(group));
    }

    // 🔹 2. 사용자 이메일로 초대
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<?> inviteUser(@PathVariable Long groupId,
                                        @RequestParam String email,
                                        @AuthenticationPrincipal String senderUsername) {
        groupInviteService.inviteUserToGroup(groupId, senderUsername, email);
        return ResponseEntity.ok().body("초대가 성공적으로 전송되었습니다.");
    }

    // 🔹 3. 내가 받은 초대 목록 조회
    @GetMapping("/invites")
    public ResponseEntity<List<Map<String, Object>>> getInvites(@AuthenticationPrincipal String username) {
        List<GroupInvite> invites = groupInviteService.getMyPendingInvites(username);

        List<Map<String, Object>> response = invites.stream().map(invite -> {
            Map<String, Object> map = new HashMap<>();
            map.put("inviteId", invite.getId());
            map.put("groupId", invite.getGroup().getId());
            map.put("groupTitle", invite.getGroup().getTitle());
            map.put("sender", invite.getSender().getUsername());
            map.put("status", invite.getStatus());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // 🔹 4. 초대 수락
    @PostMapping("/invites/{inviteId}/accept")
    public ResponseEntity<?> acceptInvite(@PathVariable Long inviteId,
                                          @AuthenticationPrincipal String username) {
        groupInviteService.acceptInvite(inviteId, username);
        return ResponseEntity.ok().body("초대를 수락했습니다.");
    }

    // 🔹 5. 초대 거절
    @PostMapping("/invites/{inviteId}/reject")
    public ResponseEntity<?> rejectInvite(@PathVariable Long inviteId,
                                          @AuthenticationPrincipal String username) {
        groupInviteService.rejectInvite(inviteId, username);
        return ResponseEntity.ok().body("초대를 거절했습니다.");
    }

    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponseDto>> getGroupMembers(@PathVariable Long groupId,
                                                                        @AuthenticationPrincipal String username) {
        List<GroupMember> members = groupService.getGroupMembers(groupId, username);
        List<GroupMemberResponseDto> response = members.stream()
                .map(GroupMemberResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<List<TodoResponseDto>> getTodosByGroup(@PathVariable Long groupId,
                                                                 @AuthenticationPrincipal String username) {
        List<Todo> todos = todoService.getTodosByGroup(groupId, username);
        List<TodoResponseDto> response = todos.stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId,
                                             @AuthenticationPrincipal String username) {
        groupService.leaveGroup(groupId, username);
        return ResponseEntity.ok("그룹에서 성공적으로 탈퇴했습니다.");
    }

    @PutMapping("/{groupId}/leader/{newLeaderUserId}")
    public ResponseEntity<String> transferLeadership(@PathVariable Long groupId,
                                                     @PathVariable Long newLeaderUserId,
                                                     @AuthenticationPrincipal String username) {
        groupService.transferLeadership(groupId, newLeaderUserId, username);
        return ResponseEntity.ok("리더가 성공적으로 변경되었습니다.");
    }




}
