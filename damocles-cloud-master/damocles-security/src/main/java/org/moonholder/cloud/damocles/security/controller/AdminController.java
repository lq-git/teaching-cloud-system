package org.moonholder.cloud.damocles.security.controller;


import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.security.entity.LoginParam;
import org.moonholder.cloud.damocles.security.feign.FileFeign;
import org.moonholder.cloud.damocles.security.service.IAdminService;
import org.moonholder.cloud.damocles.security.service.IFeignService;
import org.moonholder.cloud.damocles.security.service.IUserService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    private IAdminService adminService;
    @Resource
    private IFeignService feignService;
    @Resource
    private IUserService userService;
    @Resource
    private FileFeign fileFeign;

    @PostMapping("login")
    public ResponseEntity login(LoginParam loginParam) {
        return adminService.login(loginParam);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody User user) {
        user.setUsername(user.getEmail());

        if (!userService.isExistUser(user)) {
            if (adminService.register(user)) {
                return ResponseEntity.success("注册成功！快去登录吧");
            }
            return ResponseEntity.error("o(╥﹏╥)o出现了一些问题...");
        }
        return ResponseEntity.error("此邮箱已注册...");
    }

    @PostMapping("logout")
    @CacheEvict(value = {"authority", "user", "dict"}, allEntries = true)
    public ResponseEntity logout() {
        return ResponseEntity.success();
    }

    @GetMapping("loginUser")
    public ResponseEntity loginUser(HttpServletRequest request) {
        return ResponseEntity.success(feignService.findUserByRequest(request));
    }

    @PostMapping("changePassword")
    public ResponseEntity changePassword(HttpServletRequest request, String oldPassword, String password) {
        return adminService.changePassword(request, oldPassword, password);
    }

    @PostMapping("changeProfile")
    @CacheEvict(value = {"user"}, allEntries = true)
    public ResponseEntity changeProfile(@RequestBody User user, HttpServletRequest request) {
        return adminService.changeProfile(user, request);
    }

    @PostMapping("uploadImg")
    public ResponseEntity uploadImg(MultipartFile[] file) {
        return fileFeign.upload(file);
    }

    @PostMapping("forget")
    public ResponseEntity forget(String email) {
        return adminService.forget(email);
    }

    @PostMapping("forgetPassword")
    public ResponseEntity forgetPassword(String forgetToken, String password) {
        return adminService.forgetPassword(forgetToken, password);
    }
}
