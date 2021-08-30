package org.moonholder.cloud.damocles.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.constant.CacheConstant;
import org.moonholder.cloud.damocles.common.core.entity.Role;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.redis.service.RedisService;
import org.moonholder.cloud.damocles.common.task.DynamicScheduleTask;
import org.moonholder.cloud.damocles.security.mapper.UserMapper;
import org.moonholder.cloud.damocles.security.service.IRoleService;
import org.moonholder.cloud.damocles.security.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
    private IRoleService roleService;
    @Resource
    private RedisService redisService;
    @Resource
    private DynamicScheduleTask dynamicScheduleTask;


    @Override
    public boolean isExistUser(User user) {
        User queryUser = baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(Objects.nonNull(user), User::getUsername, user.getUsername()));
        return queryUser != null;
    }

    @Override
    @Transactional
    public boolean setRoleTheUser(User user, String roleCode) {
        Role role = roleService.findRoleByCode(roleCode);
        return baseMapper.setRoleTheUser(user.getId(), role.getId());
    }

    @Override
    public boolean updateByUsername(User user) {
        LambdaUpdateChainWrapper<User> wrapper = new LambdaUpdateChainWrapper<>(baseMapper).eq(StringUtils.hasLength(user.getUsername()), User::getUsername, user.getUsername())
                .set(StringUtils.hasLength(user.getUsername()), User::getUsername, user.getUsername())
                .set(StringUtils.hasLength(user.getNickName()), User::getNickName, user.getNickName())
                .set(StringUtils.hasLength(user.getIcon()), User::getId, user.getIcon())
                .set(Objects.nonNull(user.getStatus()), User::getStatus, user.getStatus());
        return wrapper.update();
    }

    @Override
    @Transactional
    public void userFreeze(String username, Date date) {
        String frozenFlagKey = CacheConstant.FROZEN_FLAG.concat("_").concat(username);
        User cond = User.builder().status("freeze").username(username).build();
        updateByUsername(cond);
        long time = date.getTime() - System.currentTimeMillis();
        redisService.setCacheObject(frozenFlagKey, true, time, TimeUnit.MILLISECONDS);
        dynamicScheduleTask.startTask(() -> userUnfreeze(username), date); // 定时任务 - 自动解冻
    }

    @Override
    public void userUnfreeze(String username) {
        String frozenFlagKey = CacheConstant.FROZEN_FLAG.concat("_").concat(username);
        String frozenCountKey = CacheConstant.FROZEN_COUNT.concat("_").concat(username);
        User cond = User.builder().status("normal").username(username).build();
        updateByUsername(cond);
        redisService.deleteObject(frozenFlagKey);  // 清除冻结标识 - 解冻
        redisService.deleteObject(frozenCountKey);  // 清空redis冻结次数
    }
}
