package org.moonholder.cloud.damocles.student.feign;


import org.moonholder.cloud.damocles.common.core.constant.ServiceNameConstant;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.feign.config.FeignConfig;
import org.moonholder.cloud.damocles.student.fallback.SecurityFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = ServiceNameConstant.SECURITY, fallback = SecurityFallback.class, configuration = FeignConfig.class)
public interface SecurityFeign {

    @GetMapping(value = "/findUserByRequest")
    User findUserByRequest();
}
