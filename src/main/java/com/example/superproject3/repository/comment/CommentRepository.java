package com.example.superproject3.repository.comment;

import com.example.superproject3.repository.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByPostId(Long postId, Pageable pageable);
}
