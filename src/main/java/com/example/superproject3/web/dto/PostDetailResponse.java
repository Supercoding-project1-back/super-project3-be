package com.example.superproject3.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "게시글 세부사항 응답")
public class PostDetailResponse {
    @Schema(description = "이미지1 주소")
    private String image1; // 이미지1

    @Schema(description = "이미지2 주소")
    private String image2; // 이미지2

    @Schema(description = "지도 x 좌표")
    private double x; // x 좌표

    @Schema(description = "지도 y 좌표")
    private double y; // y 좌표
}
