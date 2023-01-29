package com.example.projectbackend.controller;

import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.dto.MemberJoinDTO;
import com.example.projectbackend.service.MemberService;
import com.example.projectbackend.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> joinMember(@RequestBody MemberJoinDTO memberJoinDTO, BindingResult bindingResult)  {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("notvalid");
        }
        try {
            memberService.create(memberJoinDTO);
            return ResponseEntity.ok("");
        } catch (MemberService.MidExistException e) {
            System.out.println("exist 에러");
            return ResponseEntity.badRequest().body("exist");
        }
    }

    @PostMapping("/logoutProc")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        System.out.println("로그아웃");
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .domain("seol.site")
                .sameSite("lax")
                .httpOnly(false)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("");
    }




}
