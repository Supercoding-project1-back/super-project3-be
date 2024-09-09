package com.example.superproject3.service.user;

import com.example.superproject3.config.security.JwtTokenProvider;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.utils.KakaoApiClient;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoApiClient kakaoApiClient;

    public Map<String, Object> kakaoLoginOrSignUp(String code) {
        String email = kakaoApiClient.getEmailFromKakao(code);
        AtomicBoolean isNewUser = new AtomicBoolean(false);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    isNewUser.set(true);
                    User newUser = User.builder()
                            .email(email)
                            .password(passwordEncoder.encode("kakao_password"))
                            .nickname(email.substring(0, email.indexOf("@")))
                            .residence("kakao_residence")
                            .profile_picture("kakao_profile_picture")
                            .introduction("kakao_introduction")
                            .roles(Set.of("ROLE_USER"))
                            .build();
                    return userRepository.save(newUser);
                });

        String token = jwtTokenProvider.createToken(user.getEmail(), user.getRoles());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("isNewUser", isNewUser.get());

        return result;
    }
}