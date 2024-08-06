package com.example.superproject3.web.dto.user;

import lombok.Data;

@Data
public class UserRegistrationDto {

    private String email;
    private String password;
    private String nickname;
    private String residence;
    private String profilePicture;
    private String introduction;
}