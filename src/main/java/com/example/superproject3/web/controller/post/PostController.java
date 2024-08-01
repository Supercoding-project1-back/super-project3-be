package com.example.superproject3.web.controller.post;

import com.example.superproject3.service.post.PostService;
import com.example.superproject3.web.dto.post.PostRequest;
import com.example.superproject3.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @GetMapping("/get-all-posts")
    public ResponseEntity<Page<PostResponse>> getAllPosts(Pageable pageable){
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    @GetMapping("/get-post/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id){
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @PostMapping("/create-post")
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest){
        PostResponse response = postService.createPost(postRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
