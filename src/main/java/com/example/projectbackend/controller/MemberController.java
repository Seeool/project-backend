package com.example.projectbackend.controller;

import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.dto.MemberJoinDTO;
import com.example.projectbackend.service.MemberService;
import com.example.projectbackend.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class MemberController {
    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @GetMapping("/api/member/me")
    public ResponseEntity<?> getMember(Authentication authentication) {
        System.out.println("/api/member/me 겟요청");
        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();
        System.out.println(memberDTO);
        return ResponseEntity.ok(memberDTO);
    }

    @PostMapping("/memberJoin")
    public ResponseEntity<?> joinMember(@RequestBody MemberJoinDTO memberJoinDTO)  {
        try {
            memberService.create(memberJoinDTO);
        } catch (MemberService.MidExistException e) {
            System.out.println("exist 에러");
            return ResponseEntity.badRequest().body("exist");
        }

        return ResponseEntity.ok("");
    }




}
