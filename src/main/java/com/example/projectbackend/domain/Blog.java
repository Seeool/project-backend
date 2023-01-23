package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "member")
public class Blog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bid;
    private int category;
    private String title;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private String fileName;

    public void changeCategory(int category) {
        this.category = category;
    }
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeText(String text) {
        this.text = text;
    }
    public void changeFileName(String fileName) {
        this.fileName = fileName;
    }

}
