package com.example.projectbackend.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity {
    @Id
    private String mid;
    private String pw;
    private String name;
    private String email;
    private String address;
    private String phone;
    @Builder.Default
    @ElementCollection
    private Set<MemberRole> roleSet = new HashSet<>();
    private boolean fromSocial;
    private String uuid;
    private String fileName;

    public void changePassword(String pw) {
        this.pw = pw;
    }
    public void changeEmail(String email) {
        this.email = email;
    }
    public void addRole(MemberRole memberRole) {
        this.roleSet.add(memberRole);
    }
    public void clearRoles() {
        this.roleSet.clear();
    }
    public void changeFromSocial(boolean fromSocial) {
        this.fromSocial = fromSocial;
    }
    public void changeImage(String uuid, String fileName) {
        this.uuid = uuid;
        this.fileName = fileName;
    }


}
