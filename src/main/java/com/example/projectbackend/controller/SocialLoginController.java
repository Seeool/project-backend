package com.example.projectbackend.controller;

import com.example.projectbackend.security.NaverAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequiredArgsConstructor
public class SocialLoginController {
//    private final KakaoAuthService kakaoAuthService;
    private final NaverAuthService naverAuthService;
//    private final GoogleAuthService googleAuthService;

//    @GetMapping("/login/oauth2/code/kakao")
//    public void kakaoLogin(HttpServletRequest request) {
//        System.out.println("카카오로그인요청");
//        String code =request.getParameter("code");
//        System.out.println(code);
//        String accessToken = kakaoAuthService.getKakaoAccessToken(code);
//        kakaoAuthService.getKakaoInfo(accessToken);
//    }

    @GetMapping("/login/oauth2/code/naver")
    public ResponseEntity<?> naverLogin(HttpServletRequest request) {
        System.out.println("네이버로그인요청");
        String code = request.getParameter("code");
        System.out.println(code);
        String accessToken = naverAuthService.getNaverAccessToken(code);
        Map<String, Object> naverInfo = naverAuthService.getNaverInfo(accessToken);
        return naverAuthService.publishJwtTokens(naverInfo);
    }

//    @GetMapping("/login/oauth2/code/google")
//    public void googleLogin(HttpServletRequest request) {
//        System.out.println("구글로그인요청");
//        String code = request.getParameter("code");
//        System.out.println(code);
//        String accessToken = googleAuthService.getGoogleAccessToken(code);
//        googleAuthService.getGoogleInfo(accessToken);
//    }
}
