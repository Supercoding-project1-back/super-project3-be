package com.example.superproject3.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long messageId;
    private String content;
    private LocalDateTime createdAt;
    private Long senderId;
    private Long chatId;
}
