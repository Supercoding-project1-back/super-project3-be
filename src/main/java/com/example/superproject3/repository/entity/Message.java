package com.example.superproject3.repository.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content; // 내용

    @Column
    private LocalDateTime created_at; // 전송시간

    @Column
    private Long sender_id; // 보낸 사람

    @ManyToOne
    @JoinColumn(name = "message_id")
    private Chat chat;
}
