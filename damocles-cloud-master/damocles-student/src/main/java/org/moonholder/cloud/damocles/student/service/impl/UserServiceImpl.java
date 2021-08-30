package org.moonholder.cloud.damocles.student.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Team;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.student.feign.SecurityFeign;
import org.moonholder.cloud.damocles.student.mapper.TeamMapper;
import org.moonholder.cloud.damocles.student.mapper.UserMapper;
import org.moonholder.cloud.damocles.student.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private SecurityFeign securityFeign;
    @Resource
    private TeamMapper teamMapper;

    @Override
    public IPage<User> findUserPageFormAuth(Page<User> page, User user) {
        return baseMapper.selectUserPageFormAuth(page, user);
    }

    @Override
    public IPage<User> findUserPageFormUser(User user, Page<User> page) {
        return baseMapper.selectUserPageFormUser(page, user);
    }

    @Override
    public List<User> findUserByRoleId(Integer roleId) {
        return baseMapper.selectUserByRoleId(roleId);
    }

    @Override
    @Transactional
    public IPage<User> findNotSignUserPageByCondition(HttpServletRequest request, User user, Page<User> page) {
        User userDetails = securityFeign.findUserByRequest();
        Team team = teamMapper.selectByUserId(userDetails.getId());
        return baseMapper.selectNotSignUser(page, user, Objects.nonNull(team) ? team.getId() : null);
    }
}
