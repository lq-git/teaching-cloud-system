package org.moonholder.cloud.damocles.security.service;

import org.moonholder.cloud.damocles.common.core.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface IFeignService {

    List<String> findAuthsByToken(String token);

    User findUserByRequest(HttpServletRequest request);

    List<String> findAuthsByRequest(HttpServletRequest request);

    String findUsernameByRequest(HttpServletRequest request);
}
