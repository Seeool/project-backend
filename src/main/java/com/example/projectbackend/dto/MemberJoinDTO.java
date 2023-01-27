package com.example.projectbackend.dto;

import com.example.projectbackend.domain.MemberRole;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class MemberJoinDTO {
    private String mid;
    private String pw;
    private String name;
    private String email;
    private String address;
    private String phone;
}
