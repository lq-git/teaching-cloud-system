package org.moonholder.cloud.damocles.authority.fallback;

import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.authority.feign.SecurityFeign;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class SecurityFallback implements SecurityFeign {
    @Override
    public boolean authentication(String token, String path) {
        log.info("安全服务已降级");
        return false;
    }

    @Override
    public User findUserByRequest() {
        log.info("安全服务已降级");
        return null;
    }

    @Override
    public String findUsernameByRequest() {
        log.info("安全服务已降级");
        return null;
    }

    @Override
    public ResponseEntity userFreeze(String username, Date freezeDate) {
        log.info("安全服务已降级");
        return null;
    }

    @Override
    public ResponseEntity userUnfreeze(String username) {
        log.info("安全服务已降级");
        return null;
    }
}
