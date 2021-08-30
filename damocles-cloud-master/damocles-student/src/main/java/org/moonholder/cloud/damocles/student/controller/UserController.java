package org.moonholder.cloud.damocles.student.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.moonholder.cloud.damocles.common.core.entity.*;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.student.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;
    @Resource
    private ITeamService teamService;
    @Resource
    private IRoleService roleService;
    @Resource
    private ISignService signService;
    @Resource
    private IDispatchRecordService recordService;

    @GetMapping("userinfo/query")
    public ResponseEntity queryUserPage(User user, Page<User> page) {
        return ResponseEntity.success(userService.findUserPageFormUser(user, page));
    }

    @DeleteMapping("userinfo/delete")
    public ResponseEntity deleteUserInfo(Integer userId) {
        return userService.removeById(userId) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("userinfo/update")
    public ResponseEntity updateUserInfo(@RequestBody User user) {
        return userService.updateById(user) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("team/create")
    public ResponseEntity createTeam(@RequestBody Team team) {
        return teamService.save(team) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @DeleteMapping("team/delete")
    public ResponseEntity deleteTeam(Integer teamId) {
        return teamService.removeById(teamId) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("team/update")
    public ResponseEntity updateTeam(@RequestBody Team team) {
        return teamService.updateById(team) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("team/query")
    public ResponseEntity queryTeam(Team team, Page<Team> page) {
        return ResponseEntity.success(teamService.findTeamListByCondition(team, page));
    }

    @GetMapping("queryUserMapByRoleType")
    public ResponseEntity queryUserMapByRoleType(String roleCode) {
        Role role = roleService.findRoleByCode(roleCode);
        return ResponseEntity.success(userService.findUserByRoleId(role.getId()));
    }


    @GetMapping("team/checkMembers")
    public ResponseEntity checkMembers(Integer teamId) {
        return ResponseEntity.success(teamService.checkMembers(teamId));
    }

    @GetMapping("/team/queryMemberMap")
    public Map<String, List<?>> queryMemberMap(Integer teamId) {
        Map<String, List<?>> result = new HashMap<>();
        List<User> noGroupList = teamService.findUnGroupUserList();
        List<User> teamMemberList = teamService.checkMembers(teamId);
        noGroupList.addAll(teamMemberList);
        List<Integer> teamMemberIds = teamMemberList.stream().map(User::getId).collect(Collectors.toList());
        result.put("data", noGroupList);
        result.put("value", teamMemberIds);
        return result;
    }

    @PostMapping("team/assignMembers")
    public ResponseEntity assignMembers(Integer teamId, Integer[] userIds) {
        try {
            teamService.assignMembers(teamId, userIds);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.error();
        }
        return ResponseEntity.success();
    }

    @GetMapping("/signin")
    public ResponseEntity signList(HttpServletRequest request, User user, Page<User> page) {
        return ResponseEntity.success(userService.findNotSignUserPageByCondition(request, user, page));
    }

    @PostMapping("/signin/submit")
    public ResponseEntity signSubmit(@RequestBody Sign sign) {
        return signService.save(sign) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("signRecord/query")
    public ResponseEntity signRecord(Sign sign, Page<Sign> page) {
        return ResponseEntity.success(signService.findSignPageByCondition(page, sign));
    }

    @GetMapping("dispatchRecord/query")
    public ResponseEntity dispatchRecord(DispatchRecord record, Page<DispatchRecord> page) {
        return ResponseEntity.success(recordService.findDispatchRecordPageByCondition(page, record));
    }

    @DeleteMapping("dispatchRecord/delete")
    public ResponseEntity deleteDispatchRecord(Integer id) {
        return recordService.removeById(id) ? ResponseEntity.success() : ResponseEntity.error();
    }
}
