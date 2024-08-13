package com.example.superproject3.web.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentResponse {
    @Schema(description = "게시글 Id", example = "1")
    private Long postId;
    @Schema(description = "댓글 Id", example = "1")
    private Long commentId;
    @Schema(description = "사용자 이메일", example = "test@gmail.com")
    private String email;
    @Schema(description = "사용자 프로필 사진", example = "")
    private String profilePicture;
    @Schema(description = "댓글 내용", example = "반가워요!")
    private String content;
    @Schema(description = "댓글 게시 일자", example = "2024-02-23 00:00:00")
    private String created_at;
}
