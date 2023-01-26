package com.example.projectbackend.controller;

import com.example.projectbackend.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

//    @GetMapping
//    public ResponseEntity<?> getMember(HttpServletRequest request) {
//
//    }
}
