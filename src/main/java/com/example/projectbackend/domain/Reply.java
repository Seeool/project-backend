package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"member", "blog"})
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyNO;
    @ManyToOne(fetch = FetchType.LAZY)
    private Blog blog;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String text;

}
