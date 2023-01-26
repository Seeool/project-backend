package com.example.projectbackend.security.handler;

import com.example.projectbackend.service.MemberService;
import com.example.projectbackend.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
@RequiredArgsConstructor
public class NormalLoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("로그인 성공 핸들러 동작");
        Map<String, Object> payload = Map.of("username", authentication.getName());

        String accessToken = jwtUtil.generateToken(payload, 5);
        String refreshToken = jwtUtil.generateToken(payload, 30);

        Map<String, Object> tokenSet = Map.of("accessToken", accessToken, "refreshToken", refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain("localhost")
                .sameSite("lax")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(tokenSet);
        System.out.println("석세스 핸들러 완료");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader("Set-Cookie", cookie.toString());
        response.getWriter().println(jsonStr);


    }
}
