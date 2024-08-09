package com.example.superproject3.web.controller.chat;

import com.example.superproject3.repository.chat.Chat;
import com.example.superproject3.repository.chat.ChatRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.service.chat.ChatService;
import com.example.superproject3.service.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;

    @GetMapping("/chat")
    public ResponseEntity<List<Chat>> getChatList(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        System.out.println(customUserDetails.toString());
        String email = customUserDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();
        System.out.println(user.toString());
        List<Chat> chatList = user.getChatAsUser1();
        return ResponseEntity.ok(chatList);
    }
}
