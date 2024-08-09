package com.example.superproject3.repository.chat;

import com.example.superproject3.repository.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Chat findByUser1OrUser2(User user1, User user2);
}
