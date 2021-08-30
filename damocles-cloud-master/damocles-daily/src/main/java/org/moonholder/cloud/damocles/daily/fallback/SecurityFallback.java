package org.moonholder.cloud.damocles.daily.fallback;

import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.daily.feign.SecurityFeign;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class SecurityFallback implements SecurityFeign {
    @Override
    public User findUserByRequest(HttpServletRequest request) {
        log.info("安全服务已降级");
        return null;
    }
}
