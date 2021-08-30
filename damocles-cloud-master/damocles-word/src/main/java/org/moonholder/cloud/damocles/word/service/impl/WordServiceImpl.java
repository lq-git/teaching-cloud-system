package org.moonholder.cloud.damocles.word.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Word;
import org.moonholder.cloud.damocles.common.core.entity.WordCollectRelation;
import org.moonholder.cloud.damocles.common.core.entity.WordLikeRelation;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.word.mapper.WordCollectRelationMapper;
import org.moonholder.cloud.damocles.word.mapper.WordLikeRelationMapper;
import org.moonholder.cloud.damocles.word.mapper.WordMapper;
import org.moonholder.cloud.damocles.word.service.IWordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements IWordService {

    @Resource
    private WordLikeRelationMapper wordLikeRelationMapper;
    @Resource
    private WordCollectRelationMapper wordCollectRelationMapper;

    @Override
    public IPage<Word> findWordPageByCondition(Page<Word> page, Word word) {
        return baseMapper.selectPageByCondition(page, word);
    }

    @Override
    public IPage<Word> findMyWordPageByCondition(Page<Word> page, Word word) {
        return baseMapper.selectMyPageByCondition(page, word);
    }

    @Override
    @Transactional
    public ResponseEntity likesWord(Word word) {
        try {
            updateById(word.setLikeNum(word.getLikeNum()));
            if (word.getLikeStatus()) {
                WordLikeRelation likeRelation = WordLikeRelation.builder().userId(word.getUserId()).wordId(word.getId()).build();
                wordLikeRelationMapper.insert(likeRelation);
            } else {
                LambdaUpdateWrapper<WordLikeRelation> wrapper = Wrappers.lambdaUpdate(WordLikeRelation.class).eq(WordLikeRelation::getWordId, word.getId()).eq(WordLikeRelation::getUserId, word.getUserId());
                wordLikeRelationMapper.delete(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.error();
        }
        return ResponseEntity.success();
    }

    @Override
    @Transactional
    public ResponseEntity collectWord(Word word) {
        try {
            if (word.getCollectStatus()) {
                WordCollectRelation wordCollectRelation = WordCollectRelation.builder().userId(word.getUserId()).wordId(word.getId()).build();
                wordCollectRelationMapper.insert(wordCollectRelation);
            } else {
                LambdaUpdateWrapper<WordCollectRelation> wrapper = Wrappers.lambdaUpdate(WordCollectRelation.class).eq(WordCollectRelation::getWordId, word.getId()).eq(WordCollectRelation::getUserId, word.getUserId());
                wordCollectRelationMapper.delete(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.success();
        }
        return ResponseEntity.error();
    }
}
