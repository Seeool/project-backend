package com.example.projectbackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class TokenController {

    @GetMapping("/getAccessToken")
    public ResponseEntity<?> getAccessToken(@CookieValue(name = "refreshToken") String refreshToken, HttpServletRequest request) {
        System.out.println(refreshToken);

        return null;
    }
}
