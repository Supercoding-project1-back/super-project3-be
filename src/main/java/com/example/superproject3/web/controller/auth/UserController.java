package com.example.superproject3.web.controller.auth;

import com.example.superproject3.service.user.UserService;
import com.example.superproject3.web.dto.user.UserDto;
import com.example.superproject3.web.dto.user.UserRegistrationDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "사용자 등록", description = "사용자를 등록합니다.")
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        userService.registerNewUser(userRegistrationDto);
        return ResponseEntity.created(URI.create("/api/users/" + userRegistrationDto.getEmail()))
                .body("사용자 등록에 성공했습니다.");
    }

    @Operation(summary = "사용자 조회", description = "이메일로 사용자를 조회합니다.")
    @GetMapping("/{email}")
    public ResponseEntity<String> getUser(@PathVariable String email) {
        UserDto userDto = UserDto.fromEntity(userService.getUserByEmail(email));
        return ResponseEntity.ok("사용자 정보를 성공적으로 조회했습니다: " + userDto.toString());
    }

    @Operation(summary = "사용자 정보 업데이트", description = "이메일로 사용자를 업데이트합니다.")
    @PutMapping("/{email}")
    public ResponseEntity<String> updateUser(@PathVariable String email, @RequestBody UserRegistrationDto userRegistrationDto) {
        userService.updateUser(userService.getUserByEmail(email).getEmail(), userRegistrationDto);
        return ResponseEntity.ok("사용자 정보를 성공적으로 업데이트했습니다.");
    }

    @Operation(summary = "사용자 삭제", description = "이메일로 사용자를 삭제합니다.")
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok("사용자를 성공적으로 삭제했습니다.");
    }

}