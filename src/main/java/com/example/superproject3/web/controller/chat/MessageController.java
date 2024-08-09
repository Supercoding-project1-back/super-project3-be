package com.example.superproject3.web.controller.chat;

import com.example.superproject3.repository.chat.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("chat/message")
    public void message(Message message){
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getId(), message);
        System.out.println(message.toString());
    }
}
