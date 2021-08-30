package org.moonholder.cloud.damocles.student.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.DispatchRecord;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IDispatchRecordService extends IService<DispatchRecord> {
    boolean generateDispatchRecords(Integer teamId, List<Integer> userIds, Integer type);

    IPage<DispatchRecord> findDispatchRecordPageByCondition(Page<DispatchRecord> page, DispatchRecord record);
}
