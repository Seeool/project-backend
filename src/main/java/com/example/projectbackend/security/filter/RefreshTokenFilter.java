package com.example.projectbackend.security.filter;

import com.example.projectbackend.security.exception.RefreshTokenException;
import com.example.projectbackend.util.JWTUtil;
import com.google.gson.Gson;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if(!path.equals("/api/token/getAccessToken")) { //TokenCheckFilter와 다른 점은 이건 특정 링크경로에만 동작
            System.out.println("RefreshTokenFilter 건너 뜀");
            filterChain.doFilter(request, response);
            return; //메소드를 더 이상 실행시키지 않는다.
        }
        System.out.println("RefreshTokenFilter 동작");

        String refreshToken = "";

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            String value = cookie.getValue();
            if(name.equals("refreshToken")) {
                refreshToken = value;
            }
        }

        //RefreshToken Validation
        try {
            checkRefreshToken(refreshToken);
        }catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return; //메소드를 더 이상 실행시키지 않는다.
        }

        Map<String, Object> refreshClaims = checkRefreshToken(refreshToken);

        Integer exp = (Integer) refreshClaims.get("exp");
        Date expTime = new Date(Instant.ofEpochMilli(exp).toEpochMilli() * 1000);
        Date current = new Date(System.currentTimeMillis());

        //만료시간과 현재시간의 간격 계산
        long gapTime = (expTime.getTime() - current.getTime());
        System.out.println("현재시간 : "+current);
        System.out.println("만료시간 : "+expTime);
        System.out.println("시간 간격 : "+gapTime);
        
        //새로운 accessToken 생성
        String mid = (String) refreshClaims.get("mid");
        String accessTokenValue = jwtUtil.generateToken(Map.of("mid",mid),10);
        System.out.println("새로운 AccessToken 생성완료");
        
        String refreshTokenValue = refreshToken;
        //RefreshToken 만료시간까지의 기한 조건
        if(gapTime < 1000*60*5) {
            System.out.println("새로운 RefreshToken 필요함");
            refreshTokenValue = jwtUtil.generateToken(Map.of("mid",mid), 60 * 24 * 7);
            System.out.println("새로운 RefreshToken 생성 완료");
        }
        System.out.println("accessToken : "+accessTokenValue);
        System.out.println("refreshToken : "+refreshTokenValue);

        sendTokens(accessTokenValue, refreshTokenValue, response);
//        filterChain.doFilter(request, response); // 다음 필터 호출이 필요 없는 이유??
    }

    private Map<String, Object> checkRefreshToken(String refreshToken) throws RefreshTokenException {
        try {
            System.out.println("RefreshTokenFilter의 checkRefreshToken 동작");
            Map<String, Object> values = jwtUtil.validateToken(refreshToken);
            return values;
        }catch (ExpiredJwtException expiredJwtException) {
            System.out.println("RefreshToken 기간 만료");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.OLD_REFRESH);
        }catch (MalformedJwtException malformedJwtException) {
            System.out.println("RefreshToken Malformedexception");
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }catch (Exception exception) {
            throw new RefreshTokenException(RefreshTokenException.ErrorCase.NO_REFRESH);
        }
    }

    private void sendTokens(String accessTokenValue, String refreshTokenValue, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshTokenValue)
                .domain("seol.site")
                .sameSite("lax")
                .httpOnly(false)
                .secure(false)
                .path("/")
                .build();
        Gson gson = new Gson();
        String jsonStr = gson.toJson(Map.of("accessToken",accessTokenValue,"refreshToken",refreshTokenValue));
        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.addHeader("Set-Cookie", cookie.toString());
            response.getWriter().println(jsonStr);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
