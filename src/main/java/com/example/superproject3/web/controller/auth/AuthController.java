package com.example.superproject3.web.controller.auth;

import com.example.superproject3.web.dto.user.UserRegistrationDto;
import com.example.superproject3.web.dto.user.UserLoginDto;
import com.example.superproject3.service.user.AuthService;
import com.example.superproject3.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

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

    @Operation(summary = "카카오 로그인", description = "카카오 계정을 사용하여 로그인합니다.")
    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, HttpServletResponse response) {
        String token = authService.kakaoLogin(code);
        response.setHeader("Authorization", "Bearer " + token);
        return "Kakao login successful";
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
}
