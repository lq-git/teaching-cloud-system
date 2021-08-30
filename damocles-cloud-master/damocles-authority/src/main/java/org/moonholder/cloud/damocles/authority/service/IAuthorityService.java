package org.moonholder.cloud.damocles.authority.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Authority;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 后台用户权限表 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IAuthorityService extends IService<Authority> {

    List<Authority> getMenuData(HttpServletRequest request);

    List<Authority> findAuthorityByUsername(String username);

    Page<Authority> findAuthorityByCondition(Authority authority, Page<Authority> page);

    List<Authority> findAuthorityByRoleId(Integer roleId);

    void assignAuthority(Integer roleId, Integer[] authIds, boolean authType);

    List<Authority> getAuthTree();
}
