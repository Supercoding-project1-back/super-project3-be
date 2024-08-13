package com.example.superproject3.repository.vote;

import com.example.superproject3.repository.post.Post;
import com.example.superproject3.repository.userPost.UserVote;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String item1; // 항목1

    @Column
    private String item2; // 항목2

    @Column
    private String item3; // 항목3

    @Column
    private String item4; // 항목4

    @Column
    private int count1; // 개수1

    @Column
    private int count2; // 개수2

    @Column
    private int count3; // 개수3

    @Column
    private int count4; // 개수4

    @OneToMany(mappedBy = "vote", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserVote> userPosts = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
