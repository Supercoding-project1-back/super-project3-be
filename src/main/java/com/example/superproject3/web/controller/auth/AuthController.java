package com.example.superproject3.web.controller.auth;

import com.example.superproject3.config.security.JwtTokenProvider;
import com.example.superproject3.service.user.AuthService;
import com.example.superproject3.service.user.TokenService;
import com.example.superproject3.service.user.UserService;
import com.example.superproject3.web.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final TokenService tokenService;

    @Operation(summary = "로그아웃 (API 번호: 추가 API)", description = "사용자가 로그아웃을 합니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        try {
            tokenService.invalidateToken(jwt);

            return ResponseEntity.ok(Map.of("message", "성공적으로 로그아웃되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "로그아웃 실패", "message", e.getMessage()));
        }
    }

    @Operation(summary = "카카오 로그인 또는 자동 회원가입 (API번호: 1번)", description = "카카오 auth code를 발급받아 로그인 및 자동 회원가입 처리를 하여 JWT토큰을 발급합니다.")
    @GetMapping("kakao/callback")
    @ApiResponse(responseCode = "200", description = "카카오 회원가입 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                    {"message":"카카오 회원가입 성공","tokenInfo":{"roles":[{"authority":"ROLE_USER"}],"isNewUser":true,"email":"exampleEmail@example.com","token":"실제 JWT token 값"}}
                                }
                                """)
            ))
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) {
        try {
            Map<String, Object> result = authService.kakaoLoginOrSignUp(code);

            String token = (String) result.get("token");
            boolean isNewUser = (boolean) result.get("isNewUser");

            String redirectUri = String.format("http://localhost:3000/auth/kakao/callback?token=%s&isNewUser=%s",
                    URLEncoder.encode(token, StandardCharsets.UTF_8),
                    isNewUser);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(redirectUri));
            return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "카카오 로그인 실패", "message", e.getMessage()));
        }
    }

    @Operation(summary = "사용자 지역 정보 업데이트 (API번호: 2번)", description = "새로 가입한 사용자의 지역 입력")
    @ApiResponse(responseCode = "200", description = "거주지 정보가 업데이트되었습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                    "message": "거주지 정보가 업데이트되었습니다."
                                }""")
            ))
    @PostMapping("/update-residence")
    public ResponseEntity<?> updateResidence(@RequestHeader("Authorization") String token, @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "사용자의 거주지 정보 입력",
            required = true,
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "residence": "songpa"
                                }""")
            )
    ) @RequestBody Map<String, String> request){
        String jwt = token.replace("Bearer", "");
        String email = jwtTokenProvider.getUsername(jwt);
        String residence = request.get("residence");

        try{
            userService.updateUserResidence(email, residence);
            return ResponseEntity.ok(Map.of("message", "거주지 정보가 업데이트되었습니다."));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "카카오 로그인 실패", "message", e.getMessage()));
        }
    }
}
