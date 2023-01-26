package com.example.projectbackend.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

public class NormalLoginFilter extends AbstractAuthenticationProcessingFilter {
    public NormalLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        System.out.println("노멀로그인필터 작동");
        Map<String, Object> loginInfo = getLoginInfo(request);
        System.out.println("로그인 정보 : "+loginInfo);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                UsernamePasswordAuthenticationToken.unauthenticated(loginInfo.get("id"), loginInfo.get("pw"));
        return getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
    }

    private Map<String, Object> getLoginInfo(HttpServletRequest request) throws IOException {
        Reader reader = new InputStreamReader(request.getInputStream());
        Gson gson = new Gson();
        return gson.fromJson(reader, Map.class);
    }
}
