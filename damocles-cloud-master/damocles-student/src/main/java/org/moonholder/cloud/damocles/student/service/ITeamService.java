package org.moonholder.cloud.damocles.student.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Team;
import org.moonholder.cloud.damocles.common.core.entity.User;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface ITeamService extends IService<Team> {

    List<User> checkMembers(Integer teamId);

    void assignMembers(Integer teamId, Integer[] userIds);

    IPage<Team> findTeamListByCondition(Team team, Page<Team> page);

    List<User> findUnGroupUserList();

}
