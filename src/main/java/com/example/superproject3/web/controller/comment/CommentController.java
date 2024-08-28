package com.example.superproject3.web.controller.comment;

import com.example.superproject3.service.comment.CommentService;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.web.dto.comment.CommentRequest;
import com.example.superproject3.web.dto.comment.CommentResponse;
import com.example.superproject3.web.dto.post.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 리스트 (API 번호 : 8번)", description = "포스트 Id의 댓글 리스트를 조회합니다.")
    @GetMapping("/comments/{post-id}")
    public ResponseEntity<Page<CommentResponse>> getAllComments(@PathVariable("post-id") Long id, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentByPostId(id, pageable));
    }

    @Operation(summary = "댓글 아이디로 조회 (API 번호 : 8번)", description = "댓글 아이디로 조회합니다.")
    @GetMapping("/comment/{comment-id}")
    public ResponseEntity<CommentResponse> getCommentByCommentId(@PathVariable("comment-id") Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    @Operation(summary = "새 댓글 등록 (API 번호 : 8번)", description = "새 댓글을 등록합니다.")
    @PostMapping("/create-comment")
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.createComment(customUserDetails, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "댓글 수정 (API 번호 : 8번)", description = "댓글을 수정합니다.")
    @PostMapping("/modify-comment/{comment-id}")
    public ResponseEntity<CommentResponse> modifyComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("comment-id") Long id, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.modifyComment(customUserDetails.getUsername(), id, commentRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 삭제 (API 번호 : 8번)", description = "댓글을 삭제합니다.")
    @DeleteMapping("/delete-comment/{comment-id}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable("comment-id") Long id) {
        commentService.deleteComment(customUserDetails.getUsername(), id);
        return ResponseEntity.ok().body("글 삭제를 성공했습니다.");
    }

    @Operation(summary = "사용자별 댓글 (API 번호: 8번)", description = "사용자별 댓글을 조회합니다.")
    @GetMapping("/comments-by-user")
    public ResponseEntity<Page<CommentResponse>> getCommentsByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable) {
        return ResponseEntity.ok(commentService.getCommentsByUserEmail(customUserDetails.getUsername(), pageable));
    }
}
