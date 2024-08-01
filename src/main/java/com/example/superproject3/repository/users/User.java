package com.example.superproject3.repository.users;

import com.example.superproject3.repository.entity.Chat;
import com.example.superproject3.repository.entity.UserPost;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "userId")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname; //  닉네임

    @Column
    private String residence; // 사는 곳

    @Column
    private String profile_picture; // 프로픨 사진

    @Column
    private String introduction; // 자기소개

//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "user1", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Chat> chatAsUser1;

    @OneToMany(mappedBy = "user2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Chat> chatAsUser2;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserPost> userPosts = new ArrayList<>();
}
