package com.example.superproject3.web.controller.auth;

import com.example.superproject3.config.security.JwtTokenProvider;
import com.example.superproject3.service.user.AuthService;
import com.example.superproject3.service.user.UserService;
import com.example.superproject3.web.dto.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Operation(summary = "카카오 로그인 또는 자동 회원가입", description = "카카오 auth code를 발급받아 로그인 및 자동 회원가입 처리를 하여 JWT토큰을 발급합니다.")
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
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code){
        try{
            Map<String, Object> result = authService.kakaoLoginOrSignUp(code);

            String token = (String) result.get("token");
            boolean isNewUser = (boolean) result.get("isNewUser");

            String email = jwtTokenProvider.getUsername(token);
            List<SimpleGrantedAuthority> roles = jwtTokenProvider.getRoles(token);

            Map<String, Object> tokenInfo = new HashMap<>();
            tokenInfo.put("token", token);
            tokenInfo.put("email", email);
            tokenInfo.put("roles", roles);
            tokenInfo.put("isNewUser", isNewUser);

            return ResponseEntity.ok(Map.   of(
                    "tokenInfo", tokenInfo,
                    "message", isNewUser? "카카오 회원가입 성공":"카카오 로그인 성공")); //isNewUser가 true일 경우 유저 정보 업데이트 필요
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "카카오 로그인 실패", "message", e.getMessage()));
        }
    }

    @Operation(summary = "사용자 지역 정보 업데이트", description = "새로 가입한 사용자의 지역 입력")
    @ApiResponse(responseCode = "200", description = "거주지 정보가 업데이트되었습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                    "message": "거주지 정보가 업데이트되었습니다."
                                }
                                """)
            ))
    @PostMapping("/update-residence")
    public ResponseEntity<?> updateResidence(@RequestHeader("Authorization") String token, @RequestBody Map<String, String> request){
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
