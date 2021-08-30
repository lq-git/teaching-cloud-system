package org.moonholder.cloud.damocles.course.fallback;

import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.course.feign.SecurityFeign;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName SecurityFallback
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 21:29
 * @Version 1.0
 **/
@Component
@Slf4j
public class SecurityFallback implements SecurityFeign {
    @Override
    public User findUserByRequest(HttpServletRequest request) {
        log.info("安全服务已降级");
        return null;
    }
}
