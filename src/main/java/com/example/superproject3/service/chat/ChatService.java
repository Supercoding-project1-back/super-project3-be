package com.example.superproject3.service.chat;

import com.example.superproject3.repository.chat.ChatRepository;
import com.example.superproject3.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.superproject3.web.dto.chat.ChatDto;
import com.example.superproject3.repository.chat.Chat;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public ChatDto createChat(Long userId1, Long userId2) {
        Chat chat = Chat.builder()
                .user1(userRepository.findById(userId1).orElseThrow())
                .user2(userRepository.findById(userId2).orElseThrow())
                .build();
        Chat savedChat = chatRepository.save(chat);
        return ChatDto.builder()
                .chatroomId(savedChat.getId())
                .userId1(savedChat.getUser1().getId())
                .userId2(savedChat.getUser2().getId())
                .build();
    }

    public boolean deleteChat(Long chatId) {
        if (chatRepository.existsById(chatId)) {
            chatRepository.deleteById(chatId);
            return true;
        }
        return false;
    }
}