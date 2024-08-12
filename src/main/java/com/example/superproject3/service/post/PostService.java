package com.example.superproject3.service.post;

import com.example.superproject3.repository.userPost.UserPost;
import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.PostRepository;
import com.example.superproject3.repository.userPost.UserPostRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.service.user.CustomUserDetails;
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
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserPostRepository userPostRepository;

    public Page<PostResponse> getAllPosts(Pageable pageable){
        return userPostRepository.findAll(pageable)
                .map(r->PostResponse.builder()
                        .id(r.getId())
                        .email(r.getUser().getEmail())
                        .title(r.getPost().getTitle())
                        .content(r.getPost().getContent())
                        .category(r.getPost().getCategory())
                        .views(r.getPost().getViews())
                        .create_at(r.getPost().getCreated_at().toString())
                        .build());
    }

    public Page<PostResponse> getPostsByCategory(String category, Pageable pageable){
        return userPostRepository.findByPostCategory(category, pageable)
                .map(r->PostResponse.builder()
                        .id(r.getId())
                        .email(r.getUser().getEmail())
                        .title(r.getPost().getTitle())
                        .content(r.getPost().getContent())
                        .category(r.getPost().getCategory())
                        .views(r.getPost().getViews())
                        .create_at(r.getPost().getCreated_at().toString())
                        .build());
    }

    public Page<PostResponse> getPostsByUserEmail(String email, Pageable pageable){
        return userPostRepository.findByUserEmail(email, pageable)
                .map(r->PostResponse.builder()
                        .id(r.getId())
                        .email(r.getUser().getEmail())
                        .title(r.getPost().getTitle())
                        .content(r.getPost().getContent())
                        .category(r.getPost().getCategory())
                        .views(r.getPost().getViews())
                        .create_at(r.getPost().getCreated_at().toString())
                        .build());
    }

    public PostResponse getPostById(Long postId) {
        UserPost userPost =  userPostRepository.findByPostId(postId);

        Post post = userPost.getPost();
        post.setViews(post.getViews()+1);

        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .email(userPost.getUser().getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreated_at().toString())
                .build();
    }

    @Transactional
    public PostResponse createPost(CustomUserDetails customUserDetails, PostRequest postRequest) {
        User user = userRepository.findByEmail(customUserDetails.getUsername())
            .orElseThrow(()->new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

        if(postRequest.getTitle() == null || postRequest.getTitle().isEmpty()){
            throw new IllegalArgumentException("제목란이 공란입니다.");
        } else if (postRequest.getContent() == null || postRequest.getContent().isEmpty()){
            throw new IllegalArgumentException("내용란이 공란입니다.");
        } else if(postRequest.getCategory() == null || postRequest.getCategory().isEmpty()){
            throw new IllegalArgumentException("카테고리란이 공란입니다.");
        }

        if(!(postRequest.getCategory().equals("전체글")
        || postRequest.getCategory().equals("질문글")
        || postRequest.getCategory().equals("일상글")
        || postRequest.getCategory().equals("구매글"))){
            throw new IllegalArgumentException("게시글 카테고리명이 올바르지 않습니다.");
        }

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(postRequest.getCategory())
                .build();

        postRepository.save(post);

        UserPost userPost = UserPost.builder()
                .user(user)
                .post(post)
                .build();

        userPostRepository.save(userPost);

        return PostResponse.builder()
                .id(post.getId())
                .email(user.getEmail())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreated_at().toString())
                .build();
    }

    @Transactional
    public PostResponse modifyPost(CustomUserDetails customUserDetails, Long postId, PostRequest postRequest){
        try {
            UserPost userPost = userPostRepository.findByUserEmailAndPostId(customUserDetails.getUsername(), postId);
            Post post = postRepository.findById(userPost.getPost().getId())
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            if (postRequest.getTitle() != null && !postRequest.getTitle().isEmpty()) {
                post.setTitle(postRequest.getTitle());
            }
            if (postRequest.getContent() != null && !postRequest.getContent().isEmpty()) {
                post.setContent(postRequest.getContent());
            }
            if (postRequest.getCategory() != null && !postRequest.getCategory().isEmpty()) {
                post.setCategory(postRequest.getCategory());
            }

            postRepository.save(post);

            return PostResponse.builder()
                    .id(post.getId())
                    .email(userPost.getUser().getEmail())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .views(post.getViews())
                    .create_at(post.getCreated_at().toString())
                    .build();
        }catch (Exception e){
            throw new IllegalArgumentException("게시글 수정에 실패했습니다");
        }
    }

    @Transactional
    public void deletePost(CustomUserDetails customUserDetails, Long postId){
        try{
            User user = userRepository.findByEmail(customUserDetails.getUsername())
                    .orElseThrow(()->new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

            postRepository.deleteById(postId);
            userPostRepository.deleteByUserAndPostId(user, postId);

        } catch (Exception e){
            throw new IllegalArgumentException("글 삭제를 실패했습니다.");
        }
    }
}
