package com.example.projectbackend.service;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface MemberService {
    MemberDTO getMemberByMid(String mid);
    MemberDTO getMemberByAccessToken(Authentication authentication);

    default MemberDTO entityToDTO(Member member) {
        return MemberDTO.builder()
                .mid(member.getMid())
                .pw(member.getPw())
                .name(member.getName())
                .phone(member.getPhone())
                .email(member.getEmail())
                .address(member.getAddress())
                .roleSet(member.getRoleSet())
                .fromSocial(member.isFromSocial())
                .uuid(member.getUuid())
                .fileName(member.getFileName())
                .build();
    }
}
