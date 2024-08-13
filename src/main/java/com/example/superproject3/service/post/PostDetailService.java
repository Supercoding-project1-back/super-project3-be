package com.example.superproject3.service.post;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.PostDetailRepository;
import com.example.superproject3.repository.post.PostDetail;
import com.example.superproject3.service.mapper.PostDetailMapper;
import com.example.superproject3.web.dto.post.PostDetailRequest;
import com.example.superproject3.web.dto.post.PostDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDetailService {
    private final PostDetailRepository postDetailRepository;
    private final PostDetailMapper postDetailMapper;
    private final FileService fileService;

    // 게시글 세부사항 생성
    public PostDetail createPostDetail(PostDetailRequest postDetailRequest, Post post) {
        PostDetail postDetail = postDetailMapper.toEntity(postDetailRequest);
        if(postDetailRequest.getImage1() != null) postDetail.setImage1(fileService.createFile(postDetailRequest.getImage1()));
        if(postDetailRequest.getImage2() != null) postDetail.setImage2(fileService.createFile(postDetailRequest.getImage2()));
        postDetail.setPost(post);

        return postDetailRepository.save(postDetail);
    }

    public PostDetailResponse getPostDetailResponse(PostDetail postDetail) {
        return postDetailMapper.toResponse(postDetail);
    }

    public void deletePostDetail(Post post) {
        postDetailRepository.delete(post.getPostDetail());
    }

    public PostDetail modifyPostDetail(PostDetail postDetail, PostDetailRequest postDetailRequest) {
        if(postDetailRequest.getImage1() != null) postDetail.setImage1(fileService.createFile(postDetailRequest.getImage1()));
        if(postDetailRequest.getImage2() != null) postDetail.setImage2(fileService.createFile(postDetailRequest.getImage2()));
        if(postDetailRequest.getX() != null) postDetail.setX(postDetailRequest.getX());
        if(postDetailRequest.getY() != null) postDetail.setY(postDetailRequest.getY());

        return postDetailRepository.save(postDetail);
    }
}
