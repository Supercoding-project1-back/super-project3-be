package com.example.superproject3.service.chat;

import com.example.superproject3.repository.chat.ChatRepository;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.web.dto.chat.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.superproject3.web.dto.chat.ChatDto;
import com.example.superproject3.repository.chat.Chat;

import java.util.List;
import java.util.stream.Collectors;

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

    public ChatDto getChatById(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        List<MessageDto> messageDtos = chat.getMessages().stream()
                .map(message -> MessageDto.builder()
                        .messageId(message.getId())
                        .content(message.getContent())
                        .createdAt(message.getCreated_at())
                        .senderId(message.getSender().getId())
                        .chatId(message.getChat().getId())
                        .build())
                .collect(Collectors.toList());

        return ChatDto.builder()
                .chatroomId(chat.getId())
                .userId1(chat.getUser1().getId())
                .userId2(chat.getUser2().getId())
                .messages(messageDtos)
                .build();
    }
}