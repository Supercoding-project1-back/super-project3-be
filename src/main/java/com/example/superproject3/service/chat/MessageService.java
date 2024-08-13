package com.example.superproject3.service.chat;

import com.example.superproject3.repository.chat.Chat;
import com.example.superproject3.repository.chat.ChatRepository;
import com.example.superproject3.repository.chat.Message;
import com.example.superproject3.repository.chat.MessageRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.web.dto.chat.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public MessageDto createMessage(Long chatId, String content, Long senderId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = Message.builder()
                .chat(chat)
                .content(content)
                .created_at(LocalDateTime.now())
                .sender(sender)
                .build();

        Message savedMessage = messageRepository.save(message);

        return MessageDto.builder()
                .messageId(savedMessage.getId())
                .content(savedMessage.getContent())
                .createdAt(savedMessage.getCreated_at())
                .senderId(savedMessage.getSender().getId())
                .chatId(savedMessage.getChat().getId())
                .build();
    }

    public Optional<MessageDto> readMessage(Long messageId) {
        return messageRepository.findById(messageId)
                .map(message -> MessageDto.builder()
                        .messageId(message.getId())
                        .content(message.getContent())
                        .createdAt(message.getCreated_at())
                        .senderId(message.getSender().getId())
                        .chatId(message.getChat().getId())
                        .build());
    }

    public boolean updateMessage(Long messageId, String content) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setContent(content);
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    public boolean deleteMessage(Long messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }
}