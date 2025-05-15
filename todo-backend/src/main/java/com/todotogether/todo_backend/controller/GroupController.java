//
//package com.todotogether.todo_backend.controller;
////
//import com.todotogether.todo_backend.dto.*;
//import com.todotogether.todo_backend.entity.*;
//import com.todotogether.todo_backend.service.GroupInviteService;
//import com.todotogether.todo_backend.service.GroupService;
//import com.todotogether.todo_backend.service.TodoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
////
//@RestController
//@RequestMapping("/api/groups")
//@RequiredArgsConstructor
//public class GroupController {
//
//    private final GroupService groupService;
//    private final GroupInviteService groupInviteService;
//    private final TodoService todoService;
//
//    // 🔹 1. 그룹 생성
//    @PostMapping
//    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupRequestDto dto,
//                                                        @AuthenticationPrincipal String username) {
//        Group group = groupService.createGroup(dto, username);
//        return ResponseEntity.ok(new GroupResponseDto(group));
//    }
//
//    // 🔹 2. 사용자 이메일로 초대
//    @PostMapping("/{groupId}/invite")
//    public ResponseEntity<?> inviteUser(@PathVariable Long groupId,
//                                        @RequestBody InviteRequestDto request,
//                                        @AuthenticationPrincipal String senderUsername) {
//        groupInviteService.inviteUserToGroup(groupId, senderUsername, request.getEmail());
//        return ResponseEntity.ok().body("초대가 성공적으로 전송되었습니다.");
//    }
//
//
//
//    // 🔹 3. 내가 받은 초대 목록 조회
//    @GetMapping("/invites")
//    public ResponseEntity<List<Map<String, Object>>> getInvites(@AuthenticationPrincipal String username) {
//        List<GroupInvite> invites = groupInviteService.getMyPendingInvites(username);
//
//        List<Map<String, Object>> response = invites.stream().map(invite -> {
//            Map<String, Object> map = new HashMap<>();
//            map.put("inviteId", invite.getId());
//            map.put("groupId", invite.getGroup().getId());
//            map.put("groupTitle", invite.getGroup().getTitle());
//            map.put("sender", invite.getSender().getUsername());
//            map.put("status", invite.getStatus());
//            return map;
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.ok(response);
//    }
//
//    // 🔹 4. 초대 수락
//    @PostMapping("/invites/{inviteId}/accept")
//    public ResponseEntity<?> acceptInvite(@PathVariable Long inviteId,
//                                          @AuthenticationPrincipal String username) {
//        groupInviteService.acceptInvite(inviteId, username);
//        return ResponseEntity.ok().body("초대를 수락했습니다.");
//    }
//
//    // 🔹 5. 초대 거절
//    @PostMapping("/invites/{inviteId}/reject")
//    public ResponseEntity<?> rejectInvite(@PathVariable Long inviteId,
//                                          @AuthenticationPrincipal String username) {
//        groupInviteService.rejectInvite(inviteId, username);
//        return ResponseEntity.ok().body("초대를 거절했습니다.");
//    }
//
//    @GetMapping("/{groupId}/members")
//    public ResponseEntity<List<GroupMemberResponseDto>> getGroupMembers(@PathVariable Long groupId,
//                                                                        @AuthenticationPrincipal String username) {
//        List<GroupMemberResponseDto> members = groupService.getGroupMemberDtos(groupId, username);
//        return ResponseEntity.ok(members);
//    }
//
//    @GetMapping("/{groupId}")
//    public ResponseEntity<List<TodoResponseDto>> getTodosByGroup(@PathVariable Long groupId,
//                                                                 @AuthenticationPrincipal String username) {
//        List<Todo> todos = todoService.getTodosByGroup(groupId, username);
//        List<TodoResponseDto> response = todos.stream()
//                .map(TodoResponseDto::new)
//                .collect(Collectors.toList());
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{groupId}/leave")
//    public ResponseEntity<String> leaveGroup(@PathVariable Long groupId,
//                                             @AuthenticationPrincipal String username) {
//        groupService.leaveGroup(groupId, username);
//        return ResponseEntity.ok("그룹에서 성공적으로 탈퇴했습니다.");
//    }
//
//    @PutMapping("/{groupId}/leader/{newLeaderUserId}")
//    public ResponseEntity<String> transferLeadership(@PathVariable Long groupId,
//                                                     @PathVariable Long newLeaderUserId,
//                                                     @AuthenticationPrincipal String username) {
//        groupService.transferLeadership(groupId, newLeaderUserId, username);
//        return ResponseEntity.ok("리더가 성공적으로 변경되었습니다.");
//    }
//
//    @GetMapping("/{groupId}/me")
//    public ResponseEntity<GroupMemberResponseDto> getMyGroupInfo(@PathVariable Long groupId,
//                                                                 @AuthenticationPrincipal String username) {
//        GroupMemberResponseDto dto = groupService.getMyGroupMemberInfo(groupId, username);
//        return ResponseEntity.ok(dto);
//    }
//
//    @PutMapping("/{groupId}/members/{targetUserId}/role")
//    public ResponseEntity<Void> changeRole(@PathVariable Long groupId,
//                                           @PathVariable Long targetUserId,
//                                           @RequestParam UsersRole newRole,
//                                           @AuthenticationPrincipal String username) {
//        groupService.changeMemberAccessLevel(groupId, targetUserId, username, newRole);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/my")
//    public ResponseEntity<List<GroupResponseDto>> getMyGroups(@AuthenticationPrincipal String username) {
//        List<Group> groups = groupService.getGroupsByUsername(username);
//        List<GroupResponseDto> response = groups.stream()
//                .map(GroupResponseDto::new)
//                .toList();
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{groupId}/dashboard")
//    public ResponseEntity<DashboardResponseDto> getGroupDashboard(@PathVariable Long groupId,
//                                                                  @AuthenticationPrincipal String username) {
//        return ResponseEntity.ok(todoService.getDashboard(groupId, username));
//    }
//
//    @DeleteMapping("/{groupId}/members/{userId}")
//    public ResponseEntity<Void> kickMember(@PathVariable Long groupId,
//                                           @PathVariable Long userId,
//                                           @AuthenticationPrincipal String username) {
//        groupService.kickMember(groupId, userId, username);
//        return ResponseEntity.noContent().build();  // 204
//    }
//
//
//
//
// }


package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.*;
import com.todotogether.todo_backend.entity.*;
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
                                                        @AuthenticationPrincipal String principalUsername) {
        Group group = groupService.createGroup(dto, principalUsername);
        return ResponseEntity.ok(new GroupResponseDto(group));
    }

    // 🔹 2. 사용자 이메일로 초대
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<Map<String, String>> inviteUser(@PathVariable Long groupId,
                                                          @RequestBody InviteRequestDto request,
                                                          @AuthenticationPrincipal String principalUsername) {
        try {
            groupInviteService.inviteUserToGroup(groupId, principalUsername, request.getEmail());
            return ResponseEntity.ok(Map.of("message", "초대가 성공적으로 전송되었습니다."));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }


    // 🔹 3. 내가 받은 초대 목록 조회
    @GetMapping("/invites")
    public ResponseEntity<List<InviteResponseDto>> getInvites(@AuthenticationPrincipal String principalUsername) {
        List<GroupInvite> invites = groupInviteService.getMyPendingInvites(principalUsername);
        List<InviteResponseDto> response = invites.stream()
                .map(InviteResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }


    // 🔹 4. 초대 수락
    @PostMapping("/invites/{inviteId}/accept")
    public ResponseEntity<Map<String, String>> acceptInvite(@PathVariable Long inviteId,
                                                            @AuthenticationPrincipal String principalUsername) {
        groupInviteService.acceptInvite(inviteId, principalUsername);
        return ResponseEntity.ok(Map.of("message", "초대를 수락했습니다."));
    }

    // 🔹 5. 초대 거절
    @PostMapping("/invites/{inviteId}/reject")
    public ResponseEntity<Map<String, String>> rejectInvite(@PathVariable Long inviteId,
                                                            @AuthenticationPrincipal String principalUsername) {
        groupInviteService.rejectInvite(inviteId, principalUsername);
        return ResponseEntity.ok(Map.of("message", "초대를 거절했습니다."));
    }

    // 🔹 6. 그룹 멤버 목록 조회
    @GetMapping("/{groupId}/members")
    public ResponseEntity<List<GroupMemberResponseDto>> getGroupMembers(@PathVariable Long groupId,
                                                                        @AuthenticationPrincipal String principalUsername) {
        List<GroupMemberResponseDto> members = groupService.getGroupMemberDtos(groupId, principalUsername);
        return ResponseEntity.ok(members);
    }

    // 🔹 7. 그룹 Todo 목록 조회
    @GetMapping("/{groupId}")
    public ResponseEntity<List<TodoResponseDto>> getTodosByGroup(@PathVariable Long groupId,
                                                                 @AuthenticationPrincipal String principalUsername) {
        List<TodoResponseDto> response = todoService.getTodosByGroup(groupId, principalUsername)
                .stream()
                .map(TodoResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 8. 그룹 탈퇴
    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Map<String, String>> leaveGroup(@PathVariable Long groupId,
                                                          @AuthenticationPrincipal String principalUsername) {
        groupService.leaveGroup(groupId, principalUsername);
        return ResponseEntity.ok(Map.of("message", "그룹에서 성공적으로 탈퇴했습니다."));
    }

    // 🔹 9. 리더 위임
    @PutMapping("/{groupId}/leader/{newLeaderUserId}")
    public ResponseEntity<Map<String, String>> transferLeadership(@PathVariable Long groupId,
                                                                  @PathVariable Long newLeaderUserId,
                                                                  @AuthenticationPrincipal String principalUsername) {
        groupService.transferLeadership(groupId, newLeaderUserId, principalUsername);
        return ResponseEntity.ok(Map.of("message", "리더가 성공적으로 변경되었습니다."));
    }

    // 🔹 10. 내 그룹 내 역할 정보 조회
    @GetMapping("/{groupId}/me")
    public ResponseEntity<GroupMemberResponseDto> getMyGroupInfo(@PathVariable Long groupId,
                                                                 @AuthenticationPrincipal String principalUsername) {
        GroupMemberResponseDto dto = groupService.getMyGroupMemberInfo(groupId, principalUsername);
        return ResponseEntity.ok(dto);
    }

    // 🔹 11. 멤버 역할 변경
    @PutMapping("/{groupId}/members/{targetUserId}/role")
    public ResponseEntity<Void> changeRole(@PathVariable Long groupId,
                                           @PathVariable Long targetUserId,
                                           @RequestParam UsersRole newRole,
                                           @AuthenticationPrincipal String principalUsername) {
        groupService.changeMemberAccessLevel(groupId, targetUserId, principalUsername, newRole);
        return ResponseEntity.ok().build();
    }

    // 🔹 12. 내 그룹 목록 조회
    @GetMapping("/my")
    public ResponseEntity<List<GroupResponseDto>> getMyGroups(@AuthenticationPrincipal String principalUsername) {
        List<GroupResponseDto> response = groupService.getGroupsByUsername(principalUsername)
                .stream()
                .map(GroupResponseDto::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔹 13. 그룹 대시보드
    @GetMapping("/{groupId}/dashboard")
    public ResponseEntity<DashboardResponseDto> getGroupDashboard(@PathVariable Long groupId,
                                                                  @AuthenticationPrincipal String principalUsername) {
        return ResponseEntity.ok(todoService.getDashboard(groupId, principalUsername));
    }

    // 🔹 14. 멤버 강퇴
    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> kickMember(@PathVariable Long groupId,
                                           @PathVariable Long userId,
                                           @AuthenticationPrincipal String principalUsername) {
        groupService.kickMember(groupId, userId, principalUsername);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
