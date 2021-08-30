package org.moonholder.cloud.damocles.student.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Team;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.util.Calculator;
import org.moonholder.cloud.damocles.student.mapper.TeamMapper;
import org.moonholder.cloud.damocles.student.mapper.UserMapper;
import org.moonholder.cloud.damocles.student.service.IDispatchRecordService;
import org.moonholder.cloud.damocles.student.service.ITeamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements ITeamService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private IDispatchRecordService dispatchRecordService;

    @Override
    public List<User> checkMembers(Integer teamId) {
        return userMapper.selectUserByTeamId(teamId);
    }

    @Override
    public void assignMembers(Integer teamId, Integer[] userIds) {
        List<Integer> ownedMembers = checkMembers(teamId).stream().map(User::getId).collect(Collectors.toList());
        Integer[] ownedMemberIds = new Integer[ownedMembers.size()];
        ownedMemberIds = ownedMembers.toArray(ownedMemberIds);
        List<Integer> additionList = Calculator.calcDifference(ownedMemberIds, userIds);
        if (!additionList.isEmpty()) {
            userMapper.setUserTheTeam(teamId, additionList);
            dispatchRecordService.generateDispatchRecords(teamId, additionList, 0);
        }
        List<Integer> excludeList = Calculator.calcDifference(userIds, ownedMemberIds);
        if (!excludeList.isEmpty()) {
            userMapper.deleteUserTheTeam(teamId, excludeList);
            dispatchRecordService.generateDispatchRecords(teamId, excludeList, 1);
        }
    }

    @Override
    public IPage<Team> findTeamListByCondition(Team team, Page<Team> page) {
        return baseMapper.selectListByCondition(page, team);
    }

    @Override
    public List<User> findUnGroupUserList() {
        return baseMapper.selectUnGroupUser();
    }
}
