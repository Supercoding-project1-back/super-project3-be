package com.example.superproject3.web.dto.user;

import com.example.superproject3.repository.users.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDto {
    @Schema(description = "사용자 고유 식별자 ID", example = "1")
    private Long id;

    @Schema(description = "사용자의 이메일", example = "user@example.com")
    private String email;

    @Schema(description = "사용자의 비밀번호", example = "securepassword")
    private String password;  // 보안상 응답에 포함되지 않음

    @Schema(description = "사용자의 닉네임", example = "exampleNickname")
    private String nickname;

    @Schema(description = "사용자의 거주지", example = "Seoul")
    private String residence;

    @Schema(description = "사용자의 프로필 사진 URL", example = "http://example.com/profile.jpg")
    private String profilePicture;

    @Schema(description = "사용자의 자기소개", example = "Hello, I am an example user!")
    private String introduction;

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setResidence(user.getResidence());
        dto.setProfilePicture(user.getProfile_picture());
        dto.setIntroduction(user.getIntroduction());
        return dto;
    }

    public User toEntity() {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .residence(residence)
                .profile_picture(profilePicture)
                .introduction(introduction)
                .build();
    }
}