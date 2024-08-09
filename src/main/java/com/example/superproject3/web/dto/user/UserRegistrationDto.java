package com.example.superproject3.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserRegistrationDto {
    @Schema(description = "사용자의 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "사용자의 비밀번호", example = "securepassword")
    private String password;

    @Schema(description = "사용자의 닉네임", example = "exampleNickname")
    private String nickname;

    @Schema(description = "사용자의 거주지", example = "Seoul")
    private String residence;

    @Schema(description = "사용자의 프로필 사진 URL", example = "http://example.com/profile.jpg")
    private String profilePicture;

    @Schema(description = "사용자의 자기소개", example = "Hello, I am an example user!")
    private String introduction;
}