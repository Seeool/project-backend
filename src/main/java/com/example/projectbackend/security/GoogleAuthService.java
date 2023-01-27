package com.example.projectbackend.security;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class GoogleAuthService {
    String ClientId = "369742162754-335fdde6jif0kib2jt4kn8sjbm9r9uh9.apps.googleusercontent.com";
    String ClientSecret = "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";
    String RedirectUri = "http://localhost:9000/login/oauth2/code/google";

    public String getGoogleAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        //Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Contend-Type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("Header 생성완료");
        //Body Map이 아니라 MultiValueMap을 이용해야 한다 RestTemplet에 Map 메시지컨버터가 없다.
//        Map<String, String> body = new HashMap<>();
//        body.put("grant_type", "authorization_code");
//        body.put("client_id", ClientId);
//        body.put("redirect_uri", RedirectUri);
//        body.put("code", code);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", ClientId);
        body.add("client_secret", ClientSecret);
        body.add("redirect_uri", RedirectUri);
        body.add("code", code);
        System.out.println("Body 생성완료");

        //Http 객체 생성
        HttpEntity<?> naverTokenRequest = new HttpEntity<>(body, headers);
        System.out.println("Http 객체 생성완료");
        System.out.println("토큰 요청");
        ResponseEntity<Map> accessTokenResponse = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                naverTokenRequest,
                Map.class
        );
        System.out.println("토큰 받아옴");
        System.out.println(accessTokenResponse);
        String naverAccessToken = (String) accessTokenResponse.getBody().get("access_token");
        System.out.println(naverAccessToken);
        return naverAccessToken;
    }
    public Map<String, Object> getGoogleInfo(String googleAccessToken) {
        System.out.println("네이버 계정정보 요청 시작");
        RestTemplate rt = new RestTemplate();
        System.out.println("액세스토큰 : "+googleAccessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ googleAccessToken);
        System.out.println("헤더 생성 완료");
        HttpEntity<?> accountInfoRequest = new HttpEntity<>(headers);
        System.out.println("HttpEntity 생성 완료");
        System.out.println("계정 정보 요청");
        ResponseEntity<Map> accountInfoResponse = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                accountInfoRequest,
                Map.class
        );
        System.out.println("계정 정보 수신 완료");
        Map<String, Object> googleAccount = (Map<String, Object>) accountInfoResponse.getBody();
        System.out.println(googleAccount);
        return googleAccount;
    }


}
