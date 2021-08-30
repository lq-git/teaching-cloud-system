package org.moonholder.cloud.damocles.authority.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.authority.mapper.RoleMapper;
import org.moonholder.cloud.damocles.authority.service.IRoleService;
import org.moonholder.cloud.damocles.common.core.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Override
    public Page<Role> findRoleByCondition(Role role, Page<Role> page) {
        Page result = new LambdaQueryChainWrapper<>(baseMapper).eq(Objects.nonNull(role.getId()), Role::getId, role.getId())
                .like(StringUtils.hasLength(role.getName()), Role::getName, role.getName())
                .eq(StringUtils.hasLength(role.getCode()), Role::getCode, role.getCode())
                .page(page);
        return result;
    }

    @Override
    public Role findRoleByCode(String roleCode) {
        return getOne(new LambdaQueryWrapper<Role>().eq(StringUtils.hasLength(roleCode), Role::getCode, roleCode));
    }
}
