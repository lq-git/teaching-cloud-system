package org.moonholder.cloud.damocles.security.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Role;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IRoleService extends IService<Role> {
    Role findRoleByCode(String roleCode);
}
