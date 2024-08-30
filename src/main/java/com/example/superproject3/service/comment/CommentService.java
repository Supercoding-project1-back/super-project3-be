package com.example.superproject3.service.comment;

import com.example.superproject3.repository.comment.Comment;
import com.example.superproject3.repository.comment.CommentRepository;
import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.post.PostRepository;
import com.example.superproject3.repository.users.User;
import com.example.superproject3.repository.users.UserRepository;
import com.example.superproject3.service.user.CustomUserDetails;
import com.example.superproject3.web.dto.comment.CommentRequest;
import com.example.superproject3.web.dto.comment.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public CommentResponse createComment(CustomUserDetails customUserDetails, CommentRequest commentRequest){
        User user = userRepository.findByEmail(customUserDetails.getUsername())
                .orElseThrow(()->new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(()->new IllegalArgumentException("입력하신 게시물 Id로 게시물을 찾을 수 없습니다."));

        if (commentRequest.getContent() == null || commentRequest.getContent().isEmpty()){
            throw new IllegalArgumentException("댓글 내용란이 공란입니다.");
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(commentRequest.getContent())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.builder()
                .commentId(savedComment.getId())
                .postId(savedComment.getPost().getId())
                .nickname(savedComment.getUser().getNickname())
                .profilePicture(savedComment.getUser().getProfile_picture())
                .content(savedComment.getContent())
                .created_at(savedComment.getCreated_at().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentByPostId(Long postId, Pageable pageable){
        return commentRepository.findByPostId(postId, pageable)
                .map(c->CommentResponse.builder()
                        .postId(c.getPost().getId())
                        .commentId(c.getId())
                        .nickname(c.getUser().getNickname())
                        .profilePicture(c.getUser().getProfile_picture())
                        .content(c.getContent())
                        .created_at(c.getCreated_at().toString())
                        .build());
    }

    @Transactional(readOnly = true)
    public CommentResponse getCommentById(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()-> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        return CommentResponse.builder()
                .postId(comment.getPost().getId())
                .commentId(comment.getId())
                .nickname(comment.getUser().getNickname())
                .profilePicture(comment.getUser().getProfile_picture())
                .content(comment.getContent())
                .created_at(comment.getCreated_at().toString())
                .build();
    }

    @Transactional
    public Page<CommentResponse> getCommentsByUserEmail(String email, Pageable pageable){
        return commentRepository.findByUserEmail(email, pageable)
                .map(c->CommentResponse.builder()
                        .postId(c.getPost().getId())
                        .commentId(c.getId())
                        .nickname(c.getUser().getNickname())
                        .profilePicture(c.getUser().getProfile_picture())
                        .content(c.getContent())
                        .created_at(c.getCreated_at().toString())
                        .build());
    }

    @Transactional
    public CommentResponse modifyComment(String email, Long commentId, CommentRequest commentRequest){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new IllegalArgumentException("입력하신 댓글 Id로 게시물을 찾을 수 없습니다."));

        if(!user.getEmail().equals(comment.getUser().getEmail())){
            throw new IllegalArgumentException("댓글을 수정할 수 없습니다.");
        }

        if (commentRequest.getContent() != null && !commentRequest.getContent().isEmpty()) {
            comment.setContent(commentRequest.getContent());
        }

        commentRepository.save(comment);

        return CommentResponse.builder()
                .postId(comment.getPost().getId())
                .commentId(comment.getId())
                .nickname(comment.getUser().getNickname())
                .profilePicture(comment.getUser().getProfile_picture())
                .content(comment.getContent())
                .created_at(comment.getCreated_at().toString())
                .build();
    }

    @Transactional
    public void deleteComment(String email, Long commentId){
        try{
            User user = userRepository.findByEmail(email)
                    .orElseThrow(()->new IllegalArgumentException("입력하신 이메일로 회원을 찾을 수 없습니다."));

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

            if(!comment.getUser().getEmail().equals(user.getEmail())){
                throw new IllegalArgumentException("댓글을 삭제할 수 없습니다.");
            }

            commentRepository.delete(comment);

        } catch (Exception e){
            throw new IllegalArgumentException("글 삭제를 실패했습니다.");
        }
    }
}
