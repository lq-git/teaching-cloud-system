package org.moonholder.cloud.damocles.daily.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Daily;
import org.moonholder.cloud.damocles.common.core.entity.User;

/**
 *
 *  日报服务类
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IDailyService extends IService<Daily> {

    IPage<Daily> findDailyPageByCondition(Page<Daily> page, Daily daily);

    IPage<User> findDailySubmitRecord(Page<User> page, User user);
}
