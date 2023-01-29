package com.example.projectbackend.service;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.dto.MemberJoinDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface MemberService {
    MemberDTO getMemberByMid(String mid);
    MemberDTO getMemberByAccessToken(Authentication authentication);
    void create(MemberJoinDTO memberJoinDTO) throws MidExistException;

    static class MidExistException extends Exception {}
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
                .fileName(member.getFileName())
                .regDate(member.getRegDate())
                .modDate(member.getModDate())
                .build();
    }
}
