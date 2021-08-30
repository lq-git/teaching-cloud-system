package org.moonholder.cloud.damocles.gateway.entity;

import org.moonholder.cloud.damocles.gateway.entity.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface UserDetails extends Serializable {
    Collection<? extends GrantedAuthority> getAuthorities();
}
