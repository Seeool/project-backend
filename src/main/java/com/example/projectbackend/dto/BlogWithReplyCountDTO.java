package com.example.projectbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogWithReplyCountDTO {
    private long bid;
    private int category;
    private String title;
    private String text;
    private String mid;
    private String fileName;
    private LocalDateTime regDate, modDate;
    private Long replyCount;

}
