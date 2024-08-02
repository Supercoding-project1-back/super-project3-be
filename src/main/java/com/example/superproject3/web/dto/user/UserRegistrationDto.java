package com.example.superproject3.web.dto.user;

import lombok.Data;

@Data
public class UserRegistrationDto {

    private String email;
    private String password;
    private String nickname;
    private String location; // residence 필드에 매핑
    private String profileImage; // 프로필 이미지 URL 또는 경로
    private String introduction;
}