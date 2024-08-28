package com.example.superproject3.web.dto.post;

import com.example.superproject3.web.dto.vote.VoteResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Schema(description = "게시글 요청")
public class PostResponse {
    @Schema(description = "게시글 Id", example = "1")
    private Long id;

    @Schema(description = "사용자 닉네임", example = "tester")
    private String nickname;

    @Schema(description = "게시글 제목", example = "안녕하세요?")
    private String title;

    @Schema(description = "게시글 내용", example = "만나서 반가워요!")
    private String content;

    @Schema(description = "게시글 카테고리", example = "전체글")
    private String category;

    @Schema(description = "게시글 조회수", example = "10")
    private int views;

    @Schema(description = "게시글 게시 일자", example = "2024-09-01T00:00:00")
    private LocalDateTime create_at;

    @Schema(description = "지도 좌표와 사진")
    private PostDetailResponse postDetailResponse;

    @Schema(description = "투표")
    private VoteResponse voteResponse;
}