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

    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @PostMapping("/login")
    public String login(@Parameter(description = "로그인 정보") @RequestBody UserLoginDto loginRequest, HttpServletResponse response) {
        List<Object> tokenAndResponse = authService.login(loginRequest);
        response.setHeader("Authorization", "Bearer " + tokenAndResponse.get(0));
        return (String) tokenAndResponse.get(1);
    }

    // 액세스 토큰을 클라이언트에서 요청하는 엔드포인트
    @Operation(summary = "엑세스 토큰 요청", description = "엑세스 토큰을 요청합니다.")
    @PostMapping("/token/kakao")
    public ResponseEntity<?> requestKakaoToken(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        try {
            String token = authService.kakaoLogin(code);
            return ResponseEntity.ok(Map.of("token", token, "message", "카카오 로그인 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 로그인 실패", "message", e.getMessage()));
        }
    }

    @Operation(summary = "토큰 관련 에러", description = "토큰이 없거나 만료된 경우 메시지가 나옵니다.")
    @GetMapping("/entrypoint")
    public void entrypointException(@Parameter(description = "요청된 token 값") @RequestParam(name = "token", required = false) String token) {
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "로그인(Jwt 토큰)이 필요합니다.");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "로그인이 만료되었습니다.", new RuntimeException("유효하지 않은 토큰: " + token));
        }
    }

    @Operation(summary = "유저 권한 에러", description = "유저 권한이 올바르지 않을 때 메시지가 나옵니다.")
    @GetMapping("/access-denied")
    public void accessDeniedException(@Parameter(description = "확인할 Role 값") @RequestParam(name = "roles", required = false) String roles) {
        if (roles == null) {
            throw new AccessDeniedException("권한이 설정되지 않았습니다.");
        } else {
            throw new AccessDeniedException("권한이 없습니다. 시도한 유저의 권한: " + roles);
        }
    }

    // 인증 코드만 발급받는 엔드포인트
    @Operation(summary = "카카오 인증 코드 발급", description = "카카오 인증 코드를 발급받습니다.")
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        // 인증 코드만 반환
        return ResponseEntity.ok(Map.of("code", code, "message", "카카오 인증 코드 발급 성공"));
    }
}
