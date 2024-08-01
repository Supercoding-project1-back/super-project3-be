package com.example.superproject3.web.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Schema(description = "게시글 세부사항 요청")
public class PostDetailRequest {
    @Parameter(description = "이미지1 파일, MultipartFile 타입")
    private MultipartFile image1; // 이미지1

    @Parameter(description = "이미지2 파일, MultipartFile 타입")
    private MultipartFile image2; // 이미지2

    @Schema(description = "지도 x 좌표")
    private double x; // x 좌표

    @Schema(description = "지도 y 좌표")
    private double y; // y 좌표
}
