package org.moonholder.cloud.damocles.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Team;
import org.moonholder.cloud.damocles.common.core.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface TeamMapper extends BaseMapper<Team> {

    IPage<Team> selectListByCondition(Page<Team> page, @Param("team") Team team);

    List<User> selectUnGroupUser();

    Team selectByUserId(Integer userId);
}
