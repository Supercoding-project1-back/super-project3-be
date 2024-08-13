package com.example.superproject3.web.dto.vote;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "투표 요청")
public class VoteRequest {
    @Schema(description = "투표항목1", example = "사과")
    private String item1; // 항목1

    @Schema(description = "투표항목2", example = "딸기")
    private String item2; // 항목2

    @Schema(description = "투표항목3", example = "바나나")
    private String item3; // 항목3

    @Schema(description = "투표항목4", example = "멜론")
    private String item4; // 항목4
}
