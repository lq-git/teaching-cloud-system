package org.moonholder.cloud.damocles.security.entity;

import org.moonholder.cloud.damocles.common.core.entity.Authority;
import org.springframework.security.core.GrantedAuthority;

public class SecurityAuthority extends Authority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return this.getUri();
    }
}
