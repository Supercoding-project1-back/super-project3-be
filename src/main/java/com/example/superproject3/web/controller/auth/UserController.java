package com.example.superproject3.web.controller.auth;

import com.example.superproject3.config.security.JwtTokenProvider;
import com.example.superproject3.service.user.UserService;
import com.example.superproject3.web.dto.user.UserDto;
import com.example.superproject3.web.dto.user.UserRegistrationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    @Operation(summary = "사용자 조회", description = "사용자를 조회합니다.")
    @GetMapping("/me")
    @ApiResponse(responseCode = "200", description = "성공적으로 사용자 정보를 조회했습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserDto.class),
                    examples = @ExampleObject(value = """
                                {
                                    "user": {
                                        "email": "user@example.com",
                                        "nickname": "exampleNickname",
                                        "residence": "Seoul",
                                        "profilePicture": "http://example.com/profile.jpg",
                                        "introduction": "Hello, I am an example user!"
                                    }
                                }
                                """)
            ))
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsername(jwt);

        try{
            UserDto userDto = UserDto.fromEntity(userService.getUserByEmail(email));
            return ResponseEntity.ok("사용자 정보를 성공적으로 조회했습니다: " + userDto.toString());
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "사용자 조회 실패", "message", e.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 업데이트되었습니다.",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "message": "사용자 정보가 성공적으로 업데이트되었습니다."
                                }
                                """)
            ))
    @Operation(summary = "사용자 정보 업데이트", description = "사용자를 업데이트합니다.")
    @PutMapping("/me")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody UserRegistrationDto userRegistrationDto) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsername(jwt);

        try{
            userService.updateUser(userService.getUserByEmail(email).getEmail(), userRegistrationDto);
            return ResponseEntity.ok("사용자 정보를 성공적으로 업데이트했습니다.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "사용자 업데이트 실패", "message", e.getMessage()));
        }
    }

    @ApiResponse(responseCode = "200", description = "사용자 계정이 성공적으로 삭제되었습니다.",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                                {
                                    "message": "사용자 계정이 성공적으로 삭제되었습니다."
                                }
                                """)
            ))
    @Operation(summary = "사용자 삭제", description = "사용자를 삭제합니다.")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        String jwt = token.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsername(jwt);

        try{
            userService.deleteUser(email);
            return ResponseEntity.ok("사용자를 성공적으로 삭제했습니다.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "사용자 삭제 실패", "message", e.getMessage()));
        }
    }
}
