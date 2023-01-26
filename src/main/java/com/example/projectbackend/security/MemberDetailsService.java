package com.example.projectbackend.security;

import com.example.projectbackend.domain.Member;
import com.example.projectbackend.dto.MemberDTO;
import com.example.projectbackend.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername 동작");
        Optional<Member> result = memberRepository.findByMid(username);
        Member member = result.orElseThrow(() -> new UsernameNotFoundException("asdfasdf"));
        System.out.println("유저 존재함");
        MemberDTO memberDTO = MemberDTO.builder()
                .mid(member.getMid())
                .pw(member.getPw())
                .email(member.getEmail())
                .name(member.getName())
                .phone(member.getPhone())
                .address(member.getPhone())
                .uuid(member.getUuid())
                .fileName(member.getFileName())
                .fromSocial(member.isFromSocial())
                .roleSet(member.getRoleSet())
                .build();
        return memberDTO;
    }
}
