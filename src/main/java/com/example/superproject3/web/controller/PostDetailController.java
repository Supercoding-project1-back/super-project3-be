package com.example.superproject3.web.controller;

import com.example.superproject3.repository.PostDetailRepository;
import com.example.superproject3.repository.entity.PostDetail;
import com.example.superproject3.service.PostDetailService;
import com.example.superproject3.service.mapper.PostDetailMapper;
import com.example.superproject3.web.dto.PostDetailRequest;
import com.example.superproject3.web.dto.PostDetailResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post-detail")
@RequiredArgsConstructor
public class PostDetailController {
    private final PostDetailMapper postDetailMapper;
    private final PostDetailService postDetailService;

    @Operation(summary = "게시글 세부사항 생성", description = "게시글과 관련된 이미지, 지도 좌표 정보 생성")
    @PostMapping
    public ResponseEntity getAllItems(@Parameter(description = "게시글 세부사항 정보") PostDetailRequest postDetailRequest) {
        PostDetail postDetail = postDetailMapper.toEntity(postDetailRequest);
        postDetail = postDetailService.createPostDetail(postDetail);
        PostDetailResponse response = postDetailMapper.toResponse(postDetail);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
