package com.example.tour_backend.controller;

import com.example.tour_backend.dto.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.tour_backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRequestDto requestDto) {
//        try {
//            UserResponseDto responseDto = userService.registerUser(requestDto);
//            return ResponseEntity.ok(responseDto);
//        } catch (RuntimeException e) {
//            // 에러 처리 개선
//           return ResponseEntity.badRequest().build();
//
//        }
        UserResponseDto responseDto = userService.registerUser(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 회원 조회 (마이페이지 등)
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        return userService.getUser(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequest) {
        try {
            JwtResponse jwtResponse = userService.login(loginRequest); // 수정된 부분
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("로그인 실패: " + e.getMessage());
        }
    }
    // 회원정보 수정 추가
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateRequestDto updateRequest
    ) {
        try {
            UserResponseDto updatedUser = userService.updateUser(userId, updateRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }



    @GetMapping("/username/{username}")
    public ResponseEntity<Long> getUserIdByUsername(@PathVariable String username) {
        try {
            Long userId = userService.findUserIdByUsername(username);
            if (userId != null) {
                return ResponseEntity.ok(userId);
            } else {
                return ResponseEntity.status(404).body(null); // username 없을 때
            }
        } catch (Exception e) {
            // 예외 로그 찍고 500 응답
            System.err.println("Error while fetching userId by username: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }
}