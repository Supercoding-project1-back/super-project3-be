package com.example.superproject3.web.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "투표 여부 확인 응답")
public class UserVoteResponse {
    @Schema(description = "투표 Id", example = "1")
    private Long id;

    @Schema(description = "투표한 내용으로, item의 값을 가진다. 만약 투표를 하지 않으면 null값이다. ", example = "사과")
    private String vote_item; // 투표한 내용
}
