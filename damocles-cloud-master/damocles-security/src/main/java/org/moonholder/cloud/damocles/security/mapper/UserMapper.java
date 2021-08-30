package org.moonholder.cloud.damocles.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.security.entity.SecurityAuthority;
import org.moonholder.cloud.damocles.security.entity.SecurityUser;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    SecurityUser selectUserByUsername(String username);

    List<SecurityAuthority> selectAuthByUsername(String username);

    boolean setRoleTheUser(@Param("userId") Integer userId, @Param("roleId") Integer roleId);

}
