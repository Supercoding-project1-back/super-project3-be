package com.example.superproject3.repository.post;

import com.example.superproject3.repository.userPost.UserVote;
import com.example.superproject3.repository.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findByCategory(String category, Pageable pageable);
}
