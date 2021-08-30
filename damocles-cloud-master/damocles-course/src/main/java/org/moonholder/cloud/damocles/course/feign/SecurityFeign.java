package org.moonholder.cloud.damocles.course.feign;

import org.moonholder.cloud.damocles.common.core.constant.ServiceNameConstant;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.feign.config.FeignConfig;
import org.moonholder.cloud.damocles.course.fallback.SecurityFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = ServiceNameConstant.SECURITY,fallback = SecurityFallback.class,configuration = FeignConfig.class)
public interface SecurityFeign {

    @GetMapping("/findUserByRequest")
    User findUserByRequest(@RequestParam("request")HttpServletRequest request);
}
