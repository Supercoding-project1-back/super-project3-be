package com.example.superproject3.service.user;

import com.example.superproject3.web.dto.user.UserRegistrationDto;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 유저를 찾지 못했습니다: " + email));
    }

    public void updateUser(String email, UserRegistrationDto updateDto) {
        User user = getUserByEmail(email);

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

    @Transactional
    public void deleteUser(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 email을 가진 유저를 찾지 못했습니다: " + email);
        }
        userRepository.deleteByEmail(email);
    }

    public void updateUserResidence(String email, String residence){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("해당 email을 가진 유저를 찾지 못했습니다: " + email));
        user.setResidence(residence);
        userRepository.save(user);
    }
}
