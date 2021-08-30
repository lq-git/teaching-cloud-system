package org.moonholder.cloud.damocles.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Role;
import org.moonholder.cloud.damocles.student.mapper.RoleMapper;
import org.moonholder.cloud.damocles.student.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    public Role findRoleByCode(String roleCode) {
        return getOne(new LambdaQueryWrapper<Role>().eq(StringUtils.hasLength(roleCode), Role::getCode, roleCode));
    }
}
