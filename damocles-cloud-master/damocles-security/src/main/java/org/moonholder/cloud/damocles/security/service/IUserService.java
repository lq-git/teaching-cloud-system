package org.moonholder.cloud.damocles.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.User;

import java.util.Date;


/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IUserService extends IService<User> {
    boolean isExistUser(User user);

    boolean setRoleTheUser(User user, String roleCode);

    boolean updateByUsername(User user);

    void userFreeze(String username, Date date);

    void userUnfreeze(String username);

}
