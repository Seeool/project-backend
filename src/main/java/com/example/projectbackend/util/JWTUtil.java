package com.example.projectbackend.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWTUtil {
    private String secretKey = "1234567890qwertyuioplkjhgfdsazxcvbnm1234567890qwertyuioplkjhgfdsazxcvbnm1234567890qwertyuioplkjhgfdsazxcvbnm";
    private Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

    public String generateToken(Map<String, Object> valueMap, int minute) {

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS512");

        Map<String, Object> payload = new HashMap<>(valueMap);

        int time = 1 * minute;

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(time).toInstant()))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return jwtStr;
    }

    public Map<String, Object> validateToken(String token) {
        Map<String, Object> claim =Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claim;
    }

}
