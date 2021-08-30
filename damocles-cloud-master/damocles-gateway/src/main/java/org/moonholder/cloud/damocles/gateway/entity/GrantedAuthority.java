package org.moonholder.cloud.damocles.gateway.entity;

import java.io.Serializable;

public interface GrantedAuthority extends Serializable {
    String getAuthority();
}
