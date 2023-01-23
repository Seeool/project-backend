package com.example.projectbackend.service;

import com.example.projectbackend.domain.Blog;
import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;

import java.util.List;

public interface BlogService {
    Long create(BlogDTO blogDTO);
    BlogDTO read(Long bid);
    void update(BlogDTO blogDTO);
    void delete(Long bid);
    int getAllCount();
    int getCountBlogsByCategory(int category);
    PageResponseDTO<BlogWithReplyCountDTO> getBlogsPagination(PageRequestDTO pageRequestDTO);
    List<BlogWithReplyCountDTO> get3BlogsOrderByRegDateWithReplyCount();

    default BlogDTO entityToDTO(Blog blog) {
        return BlogDTO.builder()
                .bid(blog.getBid())
                .title(blog.getTitle())
                .text(blog.getText())
                .category(blog.getCategory())
                .fileName(blog.getFileName())
                .mid(blog.getMember().getId())
                .regDate(blog.getRegDate())
                .modDate(blog.getModDate())
                .build();
    }

    default BlogWithReplyCountDTO objectToDTO(Blog blog, Long reviewCount) {
        return BlogWithReplyCountDTO.builder()
                .bid(blog.getBid())
                .title(blog.getTitle())
                .text(blog.getText())
                .category(blog.getCategory())
                .fileName(blog.getFileName())
                .mid(blog.getMember().getId())
                .regDate(blog.getRegDate())
                .modDate(blog.getModDate())
                .replyCount(reviewCount)
                .build();
    }

    default Blog dtoToEntity(BlogDTO blogDTO) {
        return Blog.builder()
                .bid(blogDTO.getBid())
                .title(blogDTO.getTitle())
                .text(blogDTO.getText())
                .category(blogDTO.getCategory())
                .fileName(blogDTO.getFileName())
                .member(Member.builder().id(blogDTO.getMid()).build())
                .build();
    }



}
