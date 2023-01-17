package com.example.projectbackend.domain;

public enum MemberRole {
    ADMIN("ROLE_ADMIN"), MANAGER("ROLE_MANAGER"), USER("ROLE_USER");
    private String roleName;
    MemberRole(String roleName) {
        this.roleName = roleName;
    }
    public String getRoleName() {
        return this.roleName;
    }


}
