package com.example.superproject3.service.user;

import com.example.superproject3.web.dto.user.UserRegistrationDto;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 등록합니다.
     *
     * @param userRegistrationDto 회원가입 정보
     */
    public void registerNewUser(UserRegistrationDto userRegistrationDto) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // 사용자 객체 생성
        User user = User.builder()
                .email(userRegistrationDto.getEmail())
                .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                .nickname(userRegistrationDto.getNickname())
                .residence(userRegistrationDto.getResidence()) // 위치 필드 설정
                .profile_picture(userRegistrationDto.getProfilePicture()) // 프로필 이미지 필드 설정
                .introduction(userRegistrationDto.getIntroduction())
                .build();

        // 사용자 저장
        userRepository.save(user);
    }

    /**
     * 사용자 정보를 이메일로 조회합니다.
     *
     * @param email 사용자 이메일
     * @return 사용자 정보
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 유저를 찾지 못했습니다: " + email));
    }

    /**
     * 사용자 정보를 업데이트합니다.
     *
     * @param email 사용자 이메일
     * @param updateDto 업데이트할 사용자 정보
     */
    public void updateUser(String email, UserRegistrationDto updateDto) {
        User user = getUserByEmail(email);

        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        if (updateDto.getNickname() != null) {
            user.setNickname(updateDto.getNickname());
        }
        if (updateDto.getResidence() != null) {
            user.setResidence(updateDto.getResidence());
        }
        if (updateDto.getProfilePicture() != null) {
            user.setProfile_picture(updateDto.getProfilePicture());
        }
        if (updateDto.getIntroduction() != null) {
            user.setIntroduction(updateDto.getIntroduction());
        }

        userRepository.save(user);
    }

    /**
     * 사용자 정보를 삭제합니다.
     *
     * @param email 삭제할 사용자 ID
     */
    @Transactional
    public void deleteUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 email을 가진 유저를 찾지 못했습니다: " + email);
        }
        userRepository.deleteByEmail(email);
    }
}