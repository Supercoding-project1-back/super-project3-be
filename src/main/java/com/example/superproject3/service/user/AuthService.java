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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoApiClient kakaoApiClient;

    public void signUp(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("이메일이 이미 등록되어 있습니다.");
        }

        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .nickname(userRegistrationDto.getNickname())
                .residence(userRegistrationDto.getResidence())
                .profile_picture(userRegistrationDto.getProfilePicture())
                .introduction(userRegistrationDto.getIntroduction())
                .roles(Set.of("ROLE_USER"))
                .build();

        userRepository.save(user);
    }

    public List<Object> kakaoLogin(String code) {
        String email = kakaoApiClient.getEmailFromKakao(code);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode("kakao")) // 기본 비밀번호 설정
                            .roles(Set.of("ROLE_USER"))
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        return List.of(token, "카카오 로그인 성공");
    }
}