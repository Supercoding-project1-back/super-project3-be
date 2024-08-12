package com.example.superproject3.repository.userPost;

import com.example.superproject3.repository.post.Vote;
import com.example.superproject3.repository.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserVoteRepository extends JpaRepository<UserVote, Long> {

    Optional<UserVote> findByUserAndVote(User user, Vote vote);
}
