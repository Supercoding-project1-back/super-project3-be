package com.example.superproject3.service.chat;

import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;

    public User getUserWithChats(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}
