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

    private String categoty;
    private String title;
    private String text;
    @ManyToOne
    private Member member;
    private String uuid;
    private String fileName;

    public void changeCategory(String categoty) {
        this.categoty = categoty;
    }
    public void changeTitle(String title) {
        this.title = title;
    }
    public void changeText(String text) {
        this.text = text;
    }
    public void changeUuid(String uuid) {
        this.uuid = uuid;
    }
    public void changeFileName(String fileName) {
        this.fileName = fileName;
    }

}
