package com.example.projectbackend.service;

import com.example.projectbackend.config.PasswordEncoderConfig;
import com.example.projectbackend.domain.Member;
import com.example.projectbackend.domain.MemberRole;
import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.dto.MemberJoinDTO;
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
    private final PasswordEncoderConfig passwordEncoderConfig;

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
    @Override
    public void create(MemberJoinDTO memberJoinDTO) throws MidExistException {
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);
        if (exist) {
            throw new MidExistException();
        }
        Member member = Member.builder()
                .mid(memberJoinDTO.getMid())
                .pw(passwordEncoderConfig.passwordEncoder().encode(memberJoinDTO.getPw()))
                .name(memberJoinDTO.getName())
                .email(memberJoinDTO.getEmail())
                .address(memberJoinDTO.getAddress())
                .phone(memberJoinDTO.getPhone())
                .fromSocial(false)
                .build();
        member.addRole(MemberRole.USER);
        memberRepository.save(member);
    }
}
