package com.example.superproject3.web.dto.user;

import com.example.superproject3.repository.users.User;
import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String nickname;
    private String residence;
    private String profilePicture;
    private String introduction;

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setResidence(user.getResidence());
        dto.setProfilePicture(user.getProfile_picture());
        dto.setIntroduction(user.getIntroduction());
        return dto;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .residence(residence)
                .profile_picture(profilePicture)
                .introduction(introduction)
                .build();
    }
}