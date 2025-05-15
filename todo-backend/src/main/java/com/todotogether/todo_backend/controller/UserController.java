package com.todotogether.todo_backend.controller;

import com.todotogether.todo_backend.dto.PasswordChangeDto;
import com.todotogether.todo_backend.dto.UserProfileResponseDto;
import com.todotogether.todo_backend.dto.UserProfileUpdateDto;
import com.todotogether.todo_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ 내 정보 조회
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(userService.getMyProfile(username));
    }

    // ✅ 내 정보 수정
    @PutMapping("/me")
    public ResponseEntity<String> updateMyProfile(@RequestBody UserProfileUpdateDto dto,
                                                  @AuthenticationPrincipal String username) {
        userService.updateMyProfile(username, dto);
        return ResponseEntity.ok("정보가 성공적으로 수정되었습니다.");
    }

    // ✅ 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<String> deleteMyAccount(@AuthenticationPrincipal String username) {
        userService.deleteMyAccount(username);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto dto,
                                                 @AuthenticationPrincipal String username) {
        userService.changePassword(username, dto);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
    }


}
