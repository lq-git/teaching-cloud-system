package org.moonholder.cloud.damocles.student.fallback;


import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.student.feign.SecurityFeign;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityFallback implements SecurityFeign {
    @Override
    public User findUserByRequest() {
        log.info("安全服务已降级");
        return null;
    }
}
