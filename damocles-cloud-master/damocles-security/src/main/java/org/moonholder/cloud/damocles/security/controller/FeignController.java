package org.moonholder.cloud.damocles.security.controller;

import cn.hutool.core.util.ReUtil;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.security.service.IFeignService;
import org.moonholder.cloud.damocles.security.service.IUserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
public class FeignController {

    @Resource
    private IFeignService feignService;
    @Resource
    private IUserService userService;

    @GetMapping("findAuthsByRequest")
    public List<String> findAuthsByRequest(HttpServletRequest request) {
        return feignService.findAuthsByRequest(request);
    }

    @GetMapping("findUserByRequest")
    public User findUserByRequest(HttpServletRequest request) {
        return feignService.findUserByRequest(request);
    }

    @GetMapping("findUsernameByRequest")
    public String findUsernameByRequest(HttpServletRequest request) {
        return feignService.findUsernameByRequest(request);
    }

    @PostMapping("authentication")
    public boolean authentication(String token, String path) {
        List<String> auths = feignService.findAuthsByToken(token);
        return auths.stream().anyMatch(auth -> ReUtil.isMatch(path, auth));
    }

    @PostMapping("freeze")
    ResponseEntity userFreeze(@RequestParam("username") String username, @RequestParam("freezeDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'.000Z'") Date freezeDate) {
        userService.userFreeze(username, freezeDate);
        return ResponseEntity.success();
    }

    @PostMapping("unfreeze")
    ResponseEntity userUnfreeze(@RequestParam("username") String username) {
        userService.userUnfreeze(username);
        return ResponseEntity.success();
    }
}
