package org.moonholder.cloud.damocles.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.DispatchRecord;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface DispatchRecordMapper extends BaseMapper<DispatchRecord> {

    IPage<DispatchRecord> selectPageByCondition(Page<DispatchRecord> page, @Param("record") DispatchRecord dispatchRecord);
}
