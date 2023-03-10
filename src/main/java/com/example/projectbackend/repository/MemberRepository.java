package com.example.projectbackend.repository;

import com.example.projectbackend.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid = :mid")
    Optional<Member> findByMid(String mid);

    Optional<Member> findByEmail(String email);
}
