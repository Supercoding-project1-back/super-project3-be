package com.example.superproject3.service.user;

import com.example.superproject3.config.security.JwtTokenProvider;
import com.example.superproject3.web.dto.user.UserLoginDto;
import com.example.superproject3.web.dto.user.UserRegistrationDto;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.utils.KakaoApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoApiClient kakaoApiClient;

    public void signUp(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("Email is already registered");
        }

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .nickname(userRegistrationDto.getNickname())
                .residence(userRegistrationDto.getLocation()) // 위치 필드 수정
                .profile_picture(userRegistrationDto.getProfileImage())
                .introduction(userRegistrationDto.getIntroduction())
                .build();

        userRepository.save(user);
    }

    public List<Object> login(UserLoginDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        String token = jwtTokenProvider.createToken(user.getEmail());
        return List.of(token, "로그인 성공");
    }

    public String kakaoLogin(String code) {
        String email = kakaoApiClient.getEmailFromKakao(code);
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder().email(email).build()));

        return jwtTokenProvider.createToken(user.getEmail());
    }
}