package org.moonholder.cloud.damocles.student.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.DispatchRecord;
import org.moonholder.cloud.damocles.student.mapper.DispatchRecordMapper;
import org.moonholder.cloud.damocles.student.service.IDispatchRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class DispatchRecordServiceImpl extends ServiceImpl<DispatchRecordMapper, DispatchRecord> implements IDispatchRecordService {
    @Override
    public boolean generateDispatchRecords(Integer teamId, List<Integer> userIds, Integer type) {
        List<DispatchRecord> dispatchRecords = new ArrayList<>();
        for (Integer userId : userIds) {
            DispatchRecord dispatchRecord = DispatchRecord.builder().teamId(teamId).userId(userId).type(type).build();
            dispatchRecords.add(dispatchRecord);
        }
        return saveBatch(dispatchRecords);
    }

    @Override
    public IPage<DispatchRecord> findDispatchRecordPageByCondition(Page<DispatchRecord> page, DispatchRecord record) {
        return baseMapper.selectPageByCondition(page, record);
    }
}
