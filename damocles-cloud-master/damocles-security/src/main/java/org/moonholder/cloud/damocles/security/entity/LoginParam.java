package org.moonholder.cloud.damocles.security.entity;

import lombok.Data;

@Data
public class LoginParam {
    private String username;
    private String password;
    private String verifyCode;
}
