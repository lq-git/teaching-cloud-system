package org.moonholder.cloud.damocles.student.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Sign;
import org.moonholder.cloud.damocles.student.mapper.SignMapper;
import org.moonholder.cloud.damocles.student.service.ISignService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements ISignService {
    @Override
    public IPage<Sign> findSignPageByCondition(Page<Sign> page, Sign sign) {
        return baseMapper.selectPageByCondition(page, sign);
    }
}
