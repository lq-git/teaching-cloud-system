package org.moonholder.cloud.damocles.gateway.fallback;

import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.gateway.feign.SecurityFeign;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SecurityFallback implements SecurityFeign {
    @Override
    public boolean authentication(String token,String path) {
        log.info("安全服务已降级");
        return true;
    }
}
