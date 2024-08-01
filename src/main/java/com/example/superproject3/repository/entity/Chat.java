package com.example.superproject3.repository.entity;

import com.example.superproject3.repository.users.User;
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
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user1;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user2;

    @ManyToOne
    @JoinColumn(name = "user_id1")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id2")
    private User user2;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Message> messages = new ArrayList<>();
}
