package org.moonholder.cloud.damocles.authority.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.authority.mapper.UserMapper;
import org.moonholder.cloud.damocles.authority.service.IUserService;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.redis.service.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

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
    private RedisService redisService;

    @Override
    public IPage<User> findUserPageFormAuth(Page<User> page, User user) {
        return baseMapper.selectUserPageFormAuth(page, user);
    }

    @Override
    public boolean updateUserRole(Integer userId, Integer roleId) {
        String username = baseMapper.selectById(userId).getUsername();
        Collection<String> keys = redisService.keys("*::".concat(username));
        redisService.deleteObject(keys);
        return baseMapper.updateUserRole(userId, roleId) > 0;
    }
}
