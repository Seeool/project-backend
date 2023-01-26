package com.example.projectbackend.service;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.repository.MemberRepository;
import com.example.projectbackend.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    public MemberDTO getMemberByMid(String mid) {
        Optional<Member> result = memberRepository.findByMid(mid);
        Member member = result.orElseThrow();
        return entityToDTO(member);
    }

    @Override
    public MemberDTO getMemberByAccessToken(Authentication authentication) {
        authentication.getName();


        return null;
    }
}
