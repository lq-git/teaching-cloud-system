package org.moonholder.cloud.damocles.authority.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Authority;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface AuthorityMapper extends BaseMapper<Authority> {

    List<Authority> findAuthByUsername(String username);

    List<Authority> selectAuthorityByRoleId(Integer roleId);

    int setAuthTheRole(@Param("roleId") Integer roleId, @Param("authList") List<Integer> addition);


    int deleteAuthTheRole(@Param("roleId") Integer roleId, @Param("authList") List<Integer> deleteList);
}
