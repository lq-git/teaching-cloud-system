package org.moonholder.cloud.damocles.authority.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.moonholder.cloud.damocles.authority.feign.SecurityFeign;
import org.moonholder.cloud.damocles.authority.service.IAuthorityService;
import org.moonholder.cloud.damocles.authority.service.IRoleService;
import org.moonholder.cloud.damocles.authority.service.IUserService;
import org.moonholder.cloud.damocles.common.core.entity.Authority;
import org.moonholder.cloud.damocles.common.core.entity.Role;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户权限表 前端控制器
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@RestController
@RequestMapping("/authority")
public class AuthorityController {

    @Resource
    private IAuthorityService authorityService;
    @Resource
    private IRoleService roleService;
    @Resource
    private IUserService userService;
    @Resource
    private SecurityFeign securityFeign;

    @GetMapping("menuData")
    public ResponseEntity menuData(HttpServletRequest request) {
        return ResponseEntity.success(authorityService.getMenuData(request));
    }

    @GetMapping("authTree")
    public ResponseEntity authTree() {
        return ResponseEntity.success(authorityService.getAuthTree());
    }

    @GetMapping("index/query")
    public ResponseEntity query(Authority authority, Page<Authority> page) {
        return ResponseEntity.success(authorityService.findAuthorityByCondition(authority, page));
    }

    @PostMapping("index/create")
    public ResponseEntity create(@RequestBody Authority authority) {
        boolean save = authorityService.save(authority);
        return save ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("index/update")
    public ResponseEntity update(@RequestBody Authority authority) {
        boolean update = authorityService.updateById(authority);
        return update ? ResponseEntity.success() : ResponseEntity.error();
    }

    @DeleteMapping("index/delete")
    @CacheEvict(value = "authority",allEntries = true)
    public ResponseEntity delete(Integer id) {
        boolean remove = authorityService.removeById(id);
        return remove ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("user/query")
    public ResponseEntity queryUser(User user, Page<User> page) {
        return ResponseEntity.success(userService.findUserPageFormAuth(page, user));
    }

    @PostMapping("user/assignRole")
    public ResponseEntity assignRole(@RequestParam(name = "id", required = false) Integer userId, @RequestParam(name = "roleId", required = false) Integer roleId) {
        boolean flg = userService.updateUserRole(userId, roleId);
        return flg ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("user/freeze")
    public ResponseEntity userFreeze(String username, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'.000Z'") Date freezeDate) {
        return  securityFeign.userFreeze(username, freezeDate);
    }

    @PostMapping("user/unfreeze")
    public ResponseEntity userUnfreeze(String username) {
        return securityFeign.userUnfreeze(username);
    }

    @GetMapping("role/query")
    public ResponseEntity queryRole(Role role, Page<Role> page) {
        return ResponseEntity.success(roleService.findRoleByCondition(role, page));
    }

    @PostMapping("role/create")
    public ResponseEntity createRole(@RequestBody Role role) {
        boolean flg = roleService.save(role);
        return flg ? ResponseEntity.success() : ResponseEntity.error();
    }

    @DeleteMapping("role/delete")
    public ResponseEntity deleteRole(Integer roleId) {
        boolean flg = roleService.removeById(roleId);
        return flg ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("role/update")
    public ResponseEntity updateRole(@RequestBody Role role) {
        boolean flg = roleService.updateById(role);
        return flg ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("/role/queryMenuMap")
    public Map<String, List<?>> queryMenuMap(Integer roleId) {
        Map<String, List<?>> result = new HashMap<>();
        List<Authority> authorityList = authorityService.list().stream().filter(authority -> authority.getType() == 0 || authority.getType() == 1).map(item -> Authority.builder().id(item.getId()).name(item.getName()).status(item.getStatus()).build()).collect(Collectors.toList());
        List<Integer> ownedAuthIds = authorityService.findAuthorityByRoleId(roleId).stream().filter(authority -> authority.getType() == 0 || authority.getType() == 1).map(Authority::getId).collect(Collectors.toList());
        result.put("data", authorityList);
        result.put("value", ownedAuthIds);
        return result;
    }

    @GetMapping("/role/queryAuthMap")
    public Map<String, List<?>> queryAuthMap(Integer roleId) {
        Map<String, List<?>> result = new HashMap<>();
        List<Authority> authorityList = authorityService.list().stream().filter(authority -> authority.getType() == 2).map(item -> Authority.builder().id(item.getId()).name(item.getName()).status(item.getStatus()).build()).collect(Collectors.toList());
        List<Integer> ownedAuthIds = authorityService.findAuthorityByRoleId(roleId).stream().filter(authority -> authority.getType() == 2).map(Authority::getId).collect(Collectors.toList());
        result.put("data", authorityList);
        result.put("value", ownedAuthIds);
        return result;
    }

    @PostMapping("/role/assignMenu")
    public ResponseEntity assignMenu(Integer roleId, Integer[] authIds) {
        try {
            authorityService.assignAuthority(roleId, authIds, false);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.error();
        }
        return ResponseEntity.success();
    }

    @PostMapping("/role/assignAuthority")
    public ResponseEntity assignAuthority(Integer roleId, Integer[] authIds) {
        try {
            authorityService.assignAuthority(roleId, authIds, true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.error();
        }
        return ResponseEntity.success();
    }
}
