package com.example.superproject3.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserLoginDto {
    @Schema(description = "사용자의 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "사용자의 비밀번호", example = "securepassword")
    private String password;
}