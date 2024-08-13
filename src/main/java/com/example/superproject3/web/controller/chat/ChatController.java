package com.example.superproject3.web.controller.chat;

import com.example.superproject3.service.chat.ChatService;
import com.example.superproject3.web.dto.chat.ChatDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "채팅방 생성 (API 번호: 15번)", description = "두 유저 간의 채팅방을 형성합니다.")
    @PostMapping
    public ResponseEntity<ChatDto> createChat(@RequestParam Long userId1, @RequestParam Long userId2) {
        return ResponseEntity.ok(chatService.createChat(userId1, userId2));
    }

    @Operation(summary = "채팅방 삭제 (API 번호: 15번)", description = "해당 ID를 가진 채팅방을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "채팅방 삭제 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"채팅방 삭제 완료\"}")))
    @DeleteMapping("/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable Long chatId) {
        if (chatService.deleteChat(chatId)) {
            return ResponseEntity.ok("채팅방 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("채팅방 삭제 실패: 해당 ID의 채팅방을 찾을 수 없습니다.");
        }
    }
}
