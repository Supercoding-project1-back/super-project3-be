package com.example.superproject3.service.post;

import com.example.superproject3.repository.post.PostDetail;
import com.example.superproject3.repository.vote.Vote;
import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.PostRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.service.vote.VoteService;
import com.example.superproject3.web.FindUserByToken;
import com.example.superproject3.web.dto.post.PostDetailRequest;
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
    private final VoteService voteService;
    private final PostDetailService postDetailService;
    private final FindUserByToken findUserByToken;

    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(r -> PostResponse.builder()
                        .id(r.getId())
                        .nickname(r.getUser().getNickname())
                        .title(r.getTitle())
                        .content(r.getContent())
                        .category(r.getCategory())
                        .views(r.getViews())
                        .create_at(r.getCreatedAt())
                        .postDetailResponse(postDetailService.getPostDetailResponse(r.getPostDetail()))
                        .voteResponse(voteService.getVote(r.getVote()))
                        .build()
                );
    }

    public Page<PostResponse> getPostsByCategory(String category, Pageable pageable) {
        return postRepository.findByCategory(category, pageable)
                .map(r -> PostResponse.builder()
                        .id(r.getId())
                        .nickname(r.getUser().getNickname())
                        .title(r.getTitle())
                        .content(r.getContent())
                        .category(r.getCategory())
                        .views(r.getViews())
                        .create_at(r.getCreatedAt())
                        .postDetailResponse(postDetailService.getPostDetailResponse(r.getPostDetail()))
                        .voteResponse(voteService.getVote(r.getVote()))
                        .build());
    }

    public Page<PostResponse> getPostsByUserEmail(User user, Pageable pageable) {
        return postRepository.findByUser(user, pageable)
                .map(r -> PostResponse.builder()
                        .id(r.getId())
                        .nickname(r.getUser().getNickname())
                        .title(r.getTitle())
                        .content(r.getContent())
                        .category(r.getCategory())
                        .views(r.getViews())
                        .create_at(r.getCreatedAt())
                        .postDetailResponse(postDetailService.getPostDetailResponse(r.getPostDetail()))
                        .voteResponse(voteService.getVote(r.getVote()))
                        .build());
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .nickname(post.getUser().getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreatedAt())
                .postDetailResponse(postDetailService.getPostDetailResponse(post.getPostDetail()))
                .voteResponse(voteService.getVote(post.getVote()))
                .build();
    }

    @Transactional
    public PostResponse createPost(CustomUserDetails customUserDetails, PostRequest postRequest) {
        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

        if (postRequest.getTitle() == null || postRequest.getTitle().isEmpty()) {
            throw new IllegalArgumentException("제목란이 공란입니다.");
        } else if (postRequest.getContent() == null || postRequest.getContent().isEmpty()) {
            throw new IllegalArgumentException("내용란이 공란입니다.");
        } else if (postRequest.getCategory() == null || postRequest.getCategory().isEmpty()) {
            throw new IllegalArgumentException("카테고리란이 공란입니다.");
        }

        if (!(postRequest.getCategory().equals("전체글")
                || postRequest.getCategory().equals("질문글")
                || postRequest.getCategory().equals("일상글")
                || postRequest.getCategory().equals("구매글"))) {
            throw new IllegalArgumentException("게시글 카테고리명이 올바르지 않습니다.");
        }

        // POST 외
        Vote vote = null;
        if (postRequest.getVoteRequest() != null) vote = voteService.createVote(postRequest.getVoteRequest());

        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .category(postRequest.getCategory())
                .user(user)
                .vote(vote)
                .build();

        if (vote != null) vote.setPost(post);
        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .nickname(user.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreatedAt())
                .voteResponse(voteService.getVote(vote))
                .build();
    }

    @Transactional
    public PostResponse createPostDetail(User user, PostDetailRequest postDetailRequest, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("게시글를 수정할 수 없습니다.");
        }

        PostDetail postDetail = postDetailService.createPostDetail(postDetailRequest, post);
        post.setPostDetail(postDetail);
        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .nickname(user.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreatedAt())
                .postDetailResponse(postDetailService.getPostDetailResponse(postDetail))
                .voteResponse(post.getVote() != null ? voteService.getVote(post.getVote()) : null)
                .build();
    }

    @Transactional
    public PostResponse modifyPost(User user, Long postId, PostRequest postRequest) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

            if (!post.getUser().getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("게시글를 수정할 수 없습니다.");
            }

            if (postRequest.getTitle() != null && !postRequest.getTitle().isEmpty()) {
                post.setTitle(postRequest.getTitle());
            }
            if (postRequest.getContent() != null && !postRequest.getContent().isEmpty()) {
                post.setContent(postRequest.getContent());
            }
            if (postRequest.getCategory() != null && !postRequest.getCategory().isEmpty()) {
                post.setCategory(postRequest.getCategory());
            }
            if (postRequest.getVoteRequest() != null) {
                voteService.modifyVote(post.getVote(), postRequest.getVoteRequest());
            }

            postRepository.save(post);

            return PostResponse.builder()
                    .id(post.getId())
                    .nickname(user.getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .category(post.getCategory())
                    .views(post.getViews())
                    .create_at(post.getCreatedAt())
                    .postDetailResponse(postDetailService.getPostDetailResponse(post.getPostDetail()))
                    .voteResponse(voteService.getVote(post.getVote()))
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("게시글 수정에 실패했습니다");
        }
    }

    @Transactional
    public PostResponse modifyPostDetail(User user, PostDetailRequest postDetailRequest, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new IllegalArgumentException("게시글를 수정할 수 없습니다.");
        }

        PostDetail postDetail = postDetailService.modifyPostDetail(post.getPostDetail(), postDetailRequest);
        post.setPostDetail(postDetail);

        postRepository.save(post);

        return PostResponse.builder()
                .id(post.getId())
                .nickname(user.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .views(post.getViews())
                .create_at(post.getCreatedAt())
                .postDetailResponse(postDetailService.getPostDetailResponse(post.getPostDetail()))
                .voteResponse(voteService.getVote(post.getVote()))
                .build();
    }

    @Transactional
    public void deletePost(User user, Long postId) {
        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
            if (!post.getUser().getEmail().equals(user.getEmail())) {
                throw new IllegalArgumentException("게시글을 삭제할 수 없습니다.");
            }
            voteService.deleteVote(post);
            postDetailService.deletePostDetail(post);
            postRepository.delete(post);
        } catch (Exception e) {
            throw new IllegalArgumentException("글 삭제를 실패했습니다.");
        }
    }
}
