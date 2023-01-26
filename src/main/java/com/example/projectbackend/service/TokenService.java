package com.example.projectbackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface TokenService {
    String getAccessToken(String refreshToken, HttpServletRequest request, HttpServletResponse response);
}
