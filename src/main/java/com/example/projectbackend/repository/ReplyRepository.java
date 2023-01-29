package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    void deleteByBlog_Bid(Long bid);
}
