package com.example.superproject3.web;

import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FindUserByToken {
    private final UserRepository userRepository;

    public  User findUser(CustomUserDetails customUserDetails) {
        User user = userRepository.findByEmail(
                customUserDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
        );

        if(!user.getEmail().equals(customUserDetails.getUsername())) throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        return user;
    }
}
