package com.example.projectbackend.security.filter;

import com.example.projectbackend.security.MemberDetailsService;
import com.example.projectbackend.security.exception.AccessTokenException;
import com.example.projectbackend.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class AccessTokenFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI(); //경로를 주입받는 필터가 아니라서 만들어야함
        if(!path.startsWith("/api/member")) { //RefreshTokenFilter와 다른 점은 이건 startsWith인것
            System.out.println("AccessTokenFilter 건너 뜀");
            filterChain.doFilter(request, response); // 다음 필터로 진행하라는 뜻
            return;
        }
        System.out.println("AccessTokenFilter 동작");

        try {
            System.out.println("토큰 검증 시작");
            Map<String, Object> payload = validateAccessToken(request);
            System.out.println("토큰 검증 성공");
            
            //UserDetails를 이용한 JWT토큰을 스프링 시큐리티에 이용하기 위한 절차
            String mid = (String) payload.get("mid");
            System.out.println("mid : "+mid);
            UserDetails userDetails = memberDetailsService.loadUserByUsername(mid);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            
            filterChain.doFilter(request, response); // 다음 필터 호출이 필요한 이유?????
        }catch (AccessTokenException accessTokenException) {
            System.out.println("토큰 검증 실패");
            accessTokenException.sendResponseError(response);
        }
    }
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException{ //throws를 안붙여도 되지않나??
        // throws는 이 validateAccessToken메서드를 호출한곳에서 해당 exception을 처리하라고 넘겨주는것
        String headerStr = request.getHeader("Authorization");
        if(headerStr == null || headerStr.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("bearer") == false) {
            System.out.println("Bearer 타입이 아님");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        }catch (MalformedJwtException malformedJwtException) {
            System.out.println("MALFORM");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.MALFORM);
        }catch (SignatureException signatureException) {
            System.out.println("BADTYPE");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }catch (ExpiredJwtException expiredJwtException) {
            System.out.println("EXPIRED");
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.EXPIRED);
        }
    }
}
