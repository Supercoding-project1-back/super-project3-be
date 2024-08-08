package com.example.superproject3.web.controller.auth;

import com.example.superproject3.web.dto.user.UserRegistrationDto;
import com.example.superproject3.web.dto.user.UserLoginDto;
import com.example.superproject3.service.user.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원 가입", description = "회원 가입을 합니다.")
    @PostMapping("/sign-up")
    public String signUp(@Parameter(description = "새로 생성할 회원 정보") @RequestBody UserRegistrationDto signupRequest) {
        authService.signUp(signupRequest);
        return "User registration successful";
    }

    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 통해 JWT 토큰을 발급받습니다.")
    @PostMapping("/login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        try {
            List<Object> tokenAndResponse = authService.kakaoLogin(code);
            return ResponseEntity.ok(Map.of("token", tokenAndResponse.get(0), "message", tokenAndResponse.get(1)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 로그인 실패", "message", e.getMessage()));
        }
    }

    @Operation(summary = "카카오 인증 코드 발급", description = "카카오 인증 코드를 발급받습니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        // 인증 코드만 반환
        return ResponseEntity.ok(Map.of("code", code, "message", "카카오 인증 코드 발급 성공"));
    }
}
