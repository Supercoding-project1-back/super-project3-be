package com.example.superproject3.repository.users;

import com.example.superproject3.repository.chat.Chat;
import com.example.superproject3.repository.entity.UserPost;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")  // 수정: userId가 아니라 id
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)  // 이메일은 유일해야 함
    private String email; // 이메일 추가

    @Column
    private String password; //사용되지 않음

    @Column
    private String nickname; // 닉네임

    @Column
    private String residence; // 사는 곳

    @Column
    private String profile_picture; // 프로필 사진

    @Column
    private String introduction; // 자기소개

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles = new HashSet<>();

    @OneToMany(mappedBy = "user1", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Chat> chatAsUser1;

    @OneToMany(mappedBy = "user2", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Chat> chatAsUser2;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<UserPost> userPosts = new ArrayList<>();

    public User() {
        this.roles.add("ROLE_USER"); // 기본 역할
    }
}
