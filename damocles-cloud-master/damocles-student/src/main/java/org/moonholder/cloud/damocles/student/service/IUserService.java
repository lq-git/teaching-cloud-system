package org.moonholder.cloud.damocles.student.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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


    IPage<User> findUserPageFormUser(User user, Page<User> page);

    List<User> findUserByRoleId(Integer roleId);

    IPage<User> findNotSignUserPageByCondition(HttpServletRequest request, User user, Page<User> page);
}
