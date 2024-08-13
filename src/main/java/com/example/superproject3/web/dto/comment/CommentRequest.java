package com.example.superproject3.web.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommentRequest {
    @Schema(description = "게시글 ID")
    private Long postId;

    @Schema(description = "댓글 내용", example = "반가워요!")
    private String content;
}
