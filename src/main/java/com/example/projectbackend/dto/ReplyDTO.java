package com.example.projectbackend.dto;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplyDTO {
    private Long replyNO;
    private Long bid;
    private String mid;
    private String text;
    private LocalDateTime regDate, modDate;
}
