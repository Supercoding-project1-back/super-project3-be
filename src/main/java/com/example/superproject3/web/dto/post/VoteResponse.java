package com.example.superproject3.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteResponse {
    @Schema(description = "투표수 Id", example = "1")
    private Long id;

    @Schema(description = "투표항목1", example = "사과")
    private String item1; // 항목1

    @Schema(description = "투표항목2", example = "딸기")
    private String item2; // 항목2

    @Schema(description = "투표항목3", example = "바나나")
    private String item3; // 항목3

    @Schema(description = "투표항목4", example = "멜론")
    private String item4; // 항목4

    @Schema(description = "투표항목1의 개수", example = "3")
    private int count1; // 개수1

    @Schema(description = "투표항목2의 개수", example = "0")
    private int count2; // 개수2

    @Schema(description = "투표항목3의 개수", example = "10")
    private int count3; // 개수3

    @Schema(description = "투표항목4의 개수", example = "2")
    private int count4; // 개수4

    @Schema(description = "투표한 내용으로, item의 값을 가진다. 만약 투표를 하지 않으면 null값이다. ", example = "사과")
    private String vote_item; // 투표한 내용
}
