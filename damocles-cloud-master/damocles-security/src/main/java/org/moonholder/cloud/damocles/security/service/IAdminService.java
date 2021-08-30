package org.moonholder.cloud.damocles.security.service;


import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.security.entity.LoginParam;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IAdminService {
    ResponseEntity login(LoginParam loginParam);

    boolean register(User user);

    ResponseEntity changePassword(HttpServletRequest request, String oldPassword, String password);

    ResponseEntity changeProfile(User user, HttpServletRequest request);

    ResponseEntity forget(String email);

    ResponseEntity forgetPassword(String forgetToken, String password);
}
