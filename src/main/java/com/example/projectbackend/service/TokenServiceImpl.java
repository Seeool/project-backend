package com.example.projectbackend.service;

import com.example.projectbackend.security.exception.RefreshTokenException;
import com.example.projectbackend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JWTUtil jwtUtil;
    @Override
    public String getAccessToken(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
        try {
            checkRefreshToken(refreshToken);
        }catch (RefreshTokenException refreshTokenException) {
            refreshTokenException.sendResponseError(response);
            return null;
        }


        return refreshToken;
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
}
