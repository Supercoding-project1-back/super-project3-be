package com.example.superproject3.repository;

import com.example.superproject3.repository.post.PostDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostDetailRepository extends JpaRepository<PostDetail, Long> {
}