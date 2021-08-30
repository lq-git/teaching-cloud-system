package org.moonholder.cloud.damocles.security.entity;

import org.moonholder.cloud.damocles.common.core.entity.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class SecurityUser extends User {

    private List<? extends GrantedAuthority> authorities;

    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}
