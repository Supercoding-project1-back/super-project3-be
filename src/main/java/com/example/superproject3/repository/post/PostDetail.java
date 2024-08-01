package com.example.superproject3.repository.post;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PostDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String image1; // 이미지1

    @Column
    private String image2; // 이미지2

    @Column
    private double x; // x 좌표

    @Column
    private double y; // y 좌표

    @OneToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
