package com.example.projectbackend.controller;

import com.example.projectbackend.dto.*;
import com.example.projectbackend.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{bid}")
    public ResponseEntity<?> read(@PathVariable Long bid) {
        return ResponseEntity.ok(blogService.readBlogByBid(bid));
    }

    @PostMapping("/authentication/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createReview(@RequestBody BlogDTO blogDTO) {
        System.out.println(blogDTO);
        Long bid = blogService.create(blogDTO);
        Map<String, Object> result = Map.of("bid", bid);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/authentication/{bid}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> modify(@RequestBody BlogDTO blogDTO) {
        System.out.println("수정 시작");
        System.out.println(blogDTO);
        blogService.update(blogDTO);
        return ResponseEntity.ok("Modify Success");
    }

    @PostMapping("/authentication/delete") //Delete요청은 payload body가 없다
    @PreAuthorize("(principal.username == #blogDTO.mid) or hasRole('ADMIN')")
    public ResponseEntity<?> deleteReview(@RequestBody BlogDTO blogDTO) {
        System.out.println(blogDTO);
        Long bid = blogDTO.getBid();
        blogService.delete(bid);
        return ResponseEntity.ok("Delete Success");
    }
}
