package org.moonholder.cloud.damocles.authority.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.User;

/**
 * <p>
 * 后台用户表 Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-119
 */
public interface UserMapper extends BaseMapper<User> {


    boolean setRoleTheUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

    IPage<User> selectUserPageFormAuth(Page<User> page, @Param("user") User user);

    int updateUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

}
