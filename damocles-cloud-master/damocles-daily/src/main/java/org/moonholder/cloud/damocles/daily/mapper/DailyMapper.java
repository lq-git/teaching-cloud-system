package org.moonholder.cloud.damocles.daily.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Daily;
import org.moonholder.cloud.damocles.common.core.entity.User;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface DailyMapper extends BaseMapper<Daily> {

    IPage<User> selectDailySubmitState(Page<User> page, @Param("user") User user);
}
