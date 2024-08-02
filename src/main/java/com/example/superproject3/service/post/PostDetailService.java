package com.example.superproject3.service.post;

import com.example.superproject3.repository.post.PostDetailRepository;
import com.example.superproject3.repository.post.PostDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostDetailService {
    private final PostDetailRepository postDetailRepository;

    // 게시글 세부사항 생성
    public PostDetail createPostDetail(PostDetail postDetail) {
        return postDetailRepository.save(postDetail);
    }
}
