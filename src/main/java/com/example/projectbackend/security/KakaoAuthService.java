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
public class KakaoAuthService {
    String ClientId = "330d5a6ed6f604b5f4a772276ebd172c";
    String RedirectUri = "http://localhost:9000/login/oauth2/code/kakao";

    public String getKakaoAccessToken(String code) {
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
        body.add("redirect_uri", RedirectUri);
        body.add("code", code);
        System.out.println("Body 생성완료");

        //Http 객체 생성
        HttpEntity<?> kakaoTokenRequest = new HttpEntity<>(body, headers);
        System.out.println("Http 객체 생성완료");
        System.out.println("토큰 요청");
        ResponseEntity<Map> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                Map.class
        );
        System.out.println("토큰 받아옴");
        System.out.println(accessTokenResponse);
        String kakaoAccessToken = (String) accessTokenResponse.getBody().get("access_token");
        System.out.println(kakaoAccessToken);
        return kakaoAccessToken;
    }

    public Map<String, Object> getKakaoInfo(String kakaoAccessToken) {
        System.out.println("카카오 계정정보 요청 시작");
        RestTemplate rt = new RestTemplate();
        System.out.println("액세스토큰 : "+kakaoAccessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+ kakaoAccessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("헤더 생성 완료");
        HttpEntity<?> accountInfoRequest = new HttpEntity<>(headers);
        System.out.println("HttpEntity 생성 완료");
        System.out.println("계정 정보 요청");
        ResponseEntity<Map> accountInfoResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                accountInfoRequest,
                Map.class
        );
        System.out.println("계정 정보 수신 완료");
        Map<String, Object> kakaoAccount = (Map<String, Object>) accountInfoResponse.getBody();
        System.out.println(kakaoAccount);
        return kakaoAccount;


    }


}
