package org.moonholder.cloud.damocles.gateway.feign;


import org.moonholder.cloud.damocles.common.core.constant.ServiceNameConstant;
import org.moonholder.cloud.damocles.gateway.fallback.SecurityFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = ServiceNameConstant.SECURITY, fallback = SecurityFallback.class)
public interface SecurityFeign {
    @PostMapping("/authentication")
    boolean authentication(@RequestParam("token") String token, @RequestParam("path") String path);
}
