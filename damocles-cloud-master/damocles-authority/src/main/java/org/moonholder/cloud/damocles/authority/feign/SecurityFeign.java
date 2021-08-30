package org.moonholder.cloud.damocles.authority.feign;


//import org.moonholder.cloud.damocles.authority.config.FeignConfig;

import org.moonholder.cloud.damocles.authority.fallback.SecurityFallback;
import org.moonholder.cloud.damocles.common.core.constant.ServiceNameConstant;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.common.feign.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(value = ServiceNameConstant.SECURITY, fallback = SecurityFallback.class, configuration = FeignConfig.class)
public interface SecurityFeign {
    @PostMapping("/authentication")
    boolean authentication(@RequestParam("token") String token, @RequestParam("path") String path);

    @GetMapping("/findUsernameByRequest")
    String findUsernameByRequest();

    @GetMapping("/findUserByRequest")
    User findUserByRequest();

    @PostMapping("/freeze")
    ResponseEntity userFreeze(@RequestParam("username") String username, @RequestParam("freezeDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'.000Z'") Date freezeDate);

    @PostMapping("/unfreeze")
    ResponseEntity userUnfreeze(@RequestParam("username") String username);
}
