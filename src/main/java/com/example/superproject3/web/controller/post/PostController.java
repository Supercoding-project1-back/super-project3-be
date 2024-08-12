package com.example.superproject3.web.controller.post;

import com.example.superproject3.repository.users.User;
import com.example.superproject3.service.post.PostService;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.web.FindUserByToken;
import com.example.superproject3.web.dto.post.PostRequest;
import com.example.superproject3.web.dto.post.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final FindUserByToken findUserByToken;

    @Operation(summary = "게시글 리스트 (API 번호: 4번)", description = "게시글 리스트를 조회합니다.")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable){
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @Operation(summary = "카테고리별 게시글 (API 번호: 5번)", description = "카테고리별 게시글을 조회합니다.")
    @GetMapping("/posts-by-category/{category}")
    public ResponseEntity<Page<PostResponse>> getPostsByCategory(@PathVariable String category, Pageable pageable){
        return ResponseEntity.ok(postService.getPostsByCategory(category, pageable));
    }

    @Operation(summary = "게시글 아이디로 조회  (API 번호: 6번)", description = "게시글 아이디로 게시글을 조회합니다.")
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @Operation(summary = "새 게시글 등록 (API 번호: 9번)", description = "새 게시글을 등록합니다.\n image1과 image2는 이미지 사진으로 MultipartFile 타입이다.")
    @ApiResponse(
            responseCode = "201",
            description = "새로운 Item 생성 완료",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PostResponse.class)
            ))
    @PostMapping("/create-post")
    public ResponseEntity<PostResponse> createPost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                           description = "새로 생성할 게시글 정보",
                                                           required = true,
                                                           content = @Content(mediaType = "multipart/form-data", schema = @Schema(implementation = PostRequest.class))
                                                   ) PostRequest postRequest){
        PostResponse response = postService.createPost(customUserDetails, postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 수정 (API 번호: 13번)", description = "게시글을 수정합니다.")
    @PostMapping("/modify-post/{id}")
    public ResponseEntity<PostResponse> modifyPost(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id, @RequestBody PostRequest postRequest){
        User user = findUserByToken.findUser(customUserDetails);
        PostResponse response = postService.modifyPost(user, id, postRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제 (API 번호: 14번)", description = "게시글을 삭제합니다.")
    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<String> deletePost(@AuthenticationPrincipal CustomUserDetails customUserDetails, @PathVariable Long id){
        postService.deletePost(findUserByToken.findUser(customUserDetails), id);
        return ResponseEntity.ok().body("글 삭제를 성공했습니다.");
    }

    @Operation(summary = "사용자별 게시글 (API 번호: 17번)", description = "사용자별 게시글을 조회합니다.")
    @GetMapping("/posts-by-user")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(@AuthenticationPrincipal CustomUserDetails customUserDetails, Pageable pageable){
        return ResponseEntity.ok(postService.getPostsByUserEmail(findUserByToken.findUser(customUserDetails), pageable));
    }
}
