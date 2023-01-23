package com.example.projectbackend.controller;

import com.example.projectbackend.dto.BlogDTO;
import com.example.projectbackend.dto.BlogWithReplyCountDTO;
import com.example.projectbackend.dto.PageRequestDTO;
import com.example.projectbackend.dto.PageResponseDTO;
import com.example.projectbackend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blog")
public class BlogController {
    private final BlogService blogService;
    @GetMapping("/list")
    ResponseEntity<?> list(PageRequestDTO pageRequestDTO) {
        PageResponseDTO<BlogWithReplyCountDTO> result = blogService.getBlogsPagination(pageRequestDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/latestList")
    ResponseEntity<?> latestList() {
        List<BlogWithReplyCountDTO> result = blogService.get3BlogsOrderByRegDateWithReplyCount();
        return ResponseEntity.ok(result);
    }
    @GetMapping("/count")
    ResponseEntity<?> Count() {
        return ResponseEntity.ok(blogService.getAllCount());
    }
    @GetMapping("/count/{category}")
    ResponseEntity<?> CountCategory(@PathVariable("category") int category) {
        return ResponseEntity.ok(blogService.getCountBlogsByCategory(category));
    }
}
