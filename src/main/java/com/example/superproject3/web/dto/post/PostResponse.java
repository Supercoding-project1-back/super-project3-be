package com.example.superproject3.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostResponse {
    @Schema(description = "게시글 Id")
    private Long id;
    @Schema(description = "사용자 이메일")
    private String email;
    @Schema(description = "게시글 제목")
    private String title;
    @Schema(description = "게시글 내용")
    private String content;
    @Schema(description = "게시글 카테고리")
    private String category;
    @Schema(description = "게시글 게시 일자")
    private String create_at;
}