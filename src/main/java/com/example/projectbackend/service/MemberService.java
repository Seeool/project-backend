package com.example.projectbackend.service;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.MemberDTO;

public interface MemberService {
    MemberDTO getMemberByMid(String mid);

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
