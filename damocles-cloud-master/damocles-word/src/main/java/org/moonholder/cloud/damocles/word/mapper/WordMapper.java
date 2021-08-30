package org.moonholder.cloud.damocles.word.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.moonholder.cloud.damocles.common.core.entity.Word;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface WordMapper extends BaseMapper<Word> {

    IPage<Word> selectPageByCondition(Page<Word> page, @Param("word") Word word);

    IPage<Word> selectMyPageByCondition(Page<Word> page, @Param("word") Word word);
}
