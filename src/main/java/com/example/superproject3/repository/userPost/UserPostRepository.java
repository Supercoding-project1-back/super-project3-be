package com.example.superproject3.repository.userPost;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPostRepository extends JpaRepository<UserPost, Long> {
    void deleteByUserAndPostId(User user, Long postId);
    Page<UserPost> findByUserEmail(String email, Pageable pageable);
    Page<UserPost> findByPostCategory(String category, Pageable pageable);
    UserPost findByUserEmailAndPostId(String email, Long id);
    UserPost findByPostId(Long id);
}
