package com.example.superproject3.web.controller.chat;

import com.example.superproject3.service.chat.MessageService;
import com.example.superproject3.web.dto.chat.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "메세지 생성 (API 번호: 15번)", description = "senderId 유저가 보내는 메세지를 생성합니다.")
    @PostMapping
    public ResponseEntity<MessageDto> createMessage(@RequestParam Long chatId, @RequestParam String content, @RequestParam Long senderId) {
        return ResponseEntity.ok(messageService.createMessage(chatId, content, senderId));
    }

    @Operation(summary = "메세지 조회 (API 번호: 15번)", description = "messageId로 해당 메세지를 검색합니다.")
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageDto> readMessage(@PathVariable Long messageId) {
        Optional<MessageDto> messageDto = messageService.readMessage(messageId);
        return messageDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @Operation(summary = "메세지 수정 (API 번호: 15번)", description = "messageId로 해당 메세지를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "메세지 수정 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"메세지 수정 완료\"}")))
    @PutMapping("/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Long messageId, @RequestParam String content) {
        if (messageService.updateMessage(messageId, content)) {
            return ResponseEntity.ok("메세지 수정 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("메세지 수정 실패: 해당 ID의 메세지를 찾을 수 없습니다.");
        }
    }

    @Operation(summary = "메세지 삭제 (API 번호: 15번)", description = "messageId로 해당 메세지를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "메세지 삭제 완료",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"메세지 삭제 완료\"}")))
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
        if (messageService.deleteMessage(messageId)) {
            return ResponseEntity.ok("메세지 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("메세지 삭제 실패: 해당 ID의 메세지를 찾을 수 없습니다.");
        }
    }
}