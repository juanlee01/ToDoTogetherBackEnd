/*
package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.LoginRequestDto;
import com.todotogether.todo_backend.dto.LoginResponseDto;
import com.todotogether.todo_backend.dto.SignupRequestDto;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.jwt.JwtUtil;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

*/
/*    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil; // ✅ 생성자에서 초기화
    }*/
/*




    public User signup(SignupRequestDto dto) {
        // 중복 확인 (선택)
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자입니다.");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 암호화
        user.setEmail(dto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername());
        return new LoginResponseDto(token);
    }
}
*/

package com.todotogether.todo_backend.service;

import com.todotogether.todo_backend.dto.*;
import com.todotogether.todo_backend.entity.User;
import com.todotogether.todo_backend.exception.DuplicateUserException;
import com.todotogether.todo_backend.exception.UnauthorizedException;
import com.todotogether.todo_backend.exception.UserNotFoundException;
import com.todotogether.todo_backend.jwt.JwtUtil;
import com.todotogether.todo_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User signup(SignupRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new DuplicateUserException();
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.createToken(user.getUsername());
        return new LoginResponseDto(token);
    }

    public boolean isUsernameAvailable(String username) {
        return userRepository.findByUsername(username).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public UserProfileResponseDto getMyProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());
        return new UserProfileResponseDto(user);
    }

    @Transactional
    public void updateMyProfile(String username, UserProfileUpdateDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            user.setEmail(dto.getEmail());
        }
        // 비밀번호 수정 추가 가능
    }

    @Transactional
    public void deleteMyAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.delete(user);
    }
    @Transactional
    public void changePassword(String username, PasswordChangeDto dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("기존 비밀번호가 일치하지 않습니다.");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
    }



}