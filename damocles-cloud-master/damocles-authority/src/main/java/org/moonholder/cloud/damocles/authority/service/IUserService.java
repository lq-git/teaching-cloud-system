package org.moonholder.cloud.damocles.authority.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.User;


/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IUserService extends IService<User> {

    IPage<User> findUserPageFormAuth(Page<User> page, User user);

    boolean updateUserRole(Integer userId, Integer roleId);
}
