package org.moonholder.cloud.damocles.student.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.User;

import java.util.List;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-119
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<User> selectUserPageFormAuth(Page<User> page, @Param("user") User user);

    List<User> selectUserByTeamId(Integer teamId);

    int setUserTheTeam(@Param("teamId") Integer teamId, @Param("additionList") List<Integer> additionList);

    int deleteUserTheTeam(@Param("teamId") Integer teamId, @Param("excludeList") List<Integer> excludeList);

    IPage<User> selectUserPageFormUser(Page<User> page, @Param("user") User user);

    List<User> selectUserByRoleId(Integer roleId);

    IPage<User> selectNotSignUser(Page<User> page, @Param("user") User user, @Param("teamId") Integer teamId);
}
