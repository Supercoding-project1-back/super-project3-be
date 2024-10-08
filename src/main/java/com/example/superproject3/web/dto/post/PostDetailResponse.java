package com.example.superproject3.web.dto.post;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 세부사항 응답")
public class PostDetailResponse {
    @Schema(description = "이미지1 주소", example = "image1.png")
    private String image1; // 이미지1

    @Schema(description = "이미지2 주소", example = "image2.png")
    private String image2; // 이미지2

    @Schema(description = "지도 x 좌표", example = "10.32401")
    private Double x; // x 좌표

    @Schema(description = "지도 y 좌표", example = "290.19223")
    private Double y; // y 좌표
}
