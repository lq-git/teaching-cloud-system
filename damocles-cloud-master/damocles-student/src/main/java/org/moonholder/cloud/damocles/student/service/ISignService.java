package org.moonholder.cloud.damocles.student.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Sign;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface ISignService extends IService<Sign> {

    IPage<Sign> findSignPageByCondition(Page<Sign> page, Sign sign);
}
