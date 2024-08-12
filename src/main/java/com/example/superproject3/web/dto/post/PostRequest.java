package com.example.superproject3.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostRequest {
    @Schema(description = "게시글 제목", example = "안녕하세요?")
    private String title;

    @Schema(description = "게시글 내용", example = "만나서 반가워요!")
    private String content;

    @Schema(description = "게시글 카테고리", example = "전체글")
    private String category;

    @Schema(description = "지도 좌표와 사진")
    private PostDetailRequest postDetailRequest;

    @Schema(description = "투표")
    private VoteRequest voteRequest;
}
