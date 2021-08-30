package org.moonholder.cloud.damocles.word.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Word;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IWordService extends IService<Word> {

    IPage<Word> findWordPageByCondition(Page<Word> page, Word word);

    IPage<Word> findMyWordPageByCondition(Page<Word> page, Word word);

    ResponseEntity likesWord(Word word);

    ResponseEntity collectWord(Word word);

}
