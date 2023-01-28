package com.example.projectbackend.security;

import com.example.projectbackend.config.PasswordEncoderConfig;
import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.MemberRole;
import com.example.projectbackend.repository.MemberRepository;
import com.example.projectbackend.util.JWTUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoAuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JWTUtil jwtUtil;

    String ClientId = "2bd4815fa725762dad388c9d643e396f";
    String RedirectUri = "http://localhost:3000/oauthKakao";

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

    public ResponseEntity<?> publishJwtTokens(Map<String, Object> kakaoAccount) {
        Map<String, String> userInfo = (Map<String, String>) kakaoAccount.get("kakao_account");
        String id = "Kakao_" + kakaoAccount.get("id");
        String email = userInfo.get("email");
        System.out.println(id);
        System.out.println(email);
        Optional<Member> result = memberRepository.findByEmail(email);
        Member member = null;
        if (result.isEmpty()) {
            System.out.println("첫 소셜로그인, 자동회원가입 시작");
            member = Member.builder()
                    .mid(id)
                    .pw(passwordEncoderConfig.passwordEncoder().encode("1234"))
                    .name(email)
                    .email(email)
                    .phone("")
                    .address("")
                    .fromSocial(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);
        } else {
            System.out.println("첫 소셜로그인 아님");
            member = result.get();
        }

        Map<String, Object> payload = Map.of("mid", member.getMid());
        String accessToken = jwtUtil.generateToken(payload, 10);
        String refreshToken = jwtUtil.generateToken(payload, 60 * 24 * 7);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        String jsonStr = gson.toJson(keyMap);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .domain("localhost")
                .sameSite("lax")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(jsonStr);
    }

}