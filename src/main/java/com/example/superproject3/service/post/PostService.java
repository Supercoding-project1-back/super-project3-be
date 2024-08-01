package com.example.superproject3.service.post;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.PostRepository;
import com.example.superproject3.web.dto.post.PostRequest;
import com.example.superproject3.web.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Page<PostResponse> getAllPosts(Pageable pageable){
        return postRepository.findAll(pageable)
                .map(r->PostResponse.builder()
                        .id(r.getId())
                        .title(r.getTitle())
                        .content(r.getContent())
                        .category(r.getCategory())
                        .create_at(r.getCreated_at().toString())
                        .build());
    }

    public PostResponse getPostById(Long id) {
        try {
            Post post = postRepository.findById(id)
                    .orElseThrow(()-> new Exception("해당 아이디의 포스트를 찾을 수 없습니다."));

            return PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .create_at(post.getCreated_at().toString())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public PostResponse createPost(PostRequest postRequest){
        try {
            if(postRequest.getTitle().isEmpty()){
                throw new Exception("제목란이 공란입니다.");
            } else if (postRequest.getContent().isEmpty()){
                throw new Exception("내용란이 공란입니다.");
            } else if(postRequest.getCategory().isEmpty()){
                throw new Exception("카테고리란이 공란입니다.");
            }

            Post post = Post.builder()
                    .title(postRequest.getTitle())
                    .content(postRequest.getContent())
                    .category(postRequest.getCategory())
                    .build();

            postRepository.save(post);

            return PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .create_at(post.getCreated_at().toString())
                    .build();

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public void deletePost(Long id){
        try{
            postRepository.deleteById(id);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
