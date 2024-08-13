package com.example.superproject3.web.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private Long chatroomId;
    private Long userId1;
    private Long userId2;
    private List<MessageDto> messages;
}
