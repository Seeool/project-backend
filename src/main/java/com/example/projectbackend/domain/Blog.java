package com.example.projectbackend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Console;

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
        System.out.println(category);
        this.category = category;
        System.out.println(this.category);
    }
    public void changeTitle(String title) {
        System.out.println(title);
        this.title = title;
        System.out.println(this.title);
    }
    public void changeText(String text) {
        System.out.println(text);
        this.text = text;
        System.out.println(this.text);
    }
    public void changeFileName(String fileName) {
        System.out.println(fileName);
        this.fileName = fileName;
        System.out.println(this.fileName);
    }

}
