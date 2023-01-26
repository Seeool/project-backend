package com.example.projectbackend.controller;

import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.service.MemberService;
import com.example.projectbackend.util.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<?> getMember(Authentication authentication) {
        System.out.println("/api/member/me 겟요청");
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        System.out.println(memberDTO);
        return ResponseEntity.ok(memberDTO);
    }




}
