package com.example.superproject3.repository.post;

import com.example.superproject3.repository.userPost.UserVote;
import com.example.superproject3.repository.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title; // 제목

    @Column
    private String content; // 내용

    @Column
    @ColumnDefault("0")
    private int views; // 조회수

    @Column
    @CreatedDate
    private LocalDateTime created_at; // 작성시간

    @Column
    private String category; // 카테고리

    @OneToOne(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Vote vote;

    @OneToOne(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private PostDetail postDetail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
