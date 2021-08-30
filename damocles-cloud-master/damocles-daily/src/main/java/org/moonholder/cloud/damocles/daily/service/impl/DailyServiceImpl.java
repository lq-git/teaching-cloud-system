package org.moonholder.cloud.damocles.daily.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Daily;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.daily.mapper.DailyMapper;
import org.moonholder.cloud.damocles.daily.service.IDailyService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class DailyServiceImpl extends ServiceImpl<DailyMapper, Daily> implements IDailyService {

    @Override
    public IPage<Daily> findDailyPageByCondition(Page<Daily> page, Daily daily) {
        LambdaQueryChainWrapper<Daily> wrapper = new LambdaQueryChainWrapper<>(baseMapper).eq(Objects.nonNull(daily.getId()), Daily::getId, daily.getId())
                .eq(Objects.nonNull(daily.getUserId()), Daily::getUserId, daily.getUserId())
                .eq(StringUtils.hasLength(daily.getStatus()), Daily::getStatus, daily.getStatus());
        return wrapper.page(page);
    }

    @Override
    public IPage<User> findDailySubmitRecord(Page<User> page, User user) {
        return baseMapper.selectDailySubmitState(page, user);
    }
}
