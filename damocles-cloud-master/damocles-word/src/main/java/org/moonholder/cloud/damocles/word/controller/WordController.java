package org.moonholder.cloud.damocles.word.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.Word;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.word.feign.SecurityFeign;
import org.moonholder.cloud.damocles.word.service.IWordService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@RestController
@RequestMapping("/word")
public class WordController {

    @Resource
    private IWordService wordService;
    @Resource
    private SecurityFeign securityFeign;

    @GetMapping("record/query")
    public ResponseEntity queryWordRecord(HttpServletRequest request, Page<Word> page, Word word) {
        return ResponseEntity.success(wordService.findWordPageByCondition(page, obtainRequestSetWordUserId(request, word)));
    }

    @PostMapping("record/like")
    public ResponseEntity likesWord(HttpServletRequest request, @RequestBody Word word) {
        return wordService.likesWord(obtainRequestSetWordUserId(request, word));
    }

    @PostMapping("record/collect")
    public ResponseEntity collectWord(HttpServletRequest request, @RequestBody Word word) {
        return wordService.collectWord(obtainRequestSetWordUserId(request, word));
    }

    @PostMapping("myWord/create")
    public ResponseEntity createWord(HttpServletRequest request, @RequestBody Word word) {
        return wordService.save(obtainRequestSetWordUserId(request, word)) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("myWord/update")
    public ResponseEntity editWord(@RequestBody Word word) {
        return wordService.updateById(word) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @DeleteMapping("myWord/delete")
    public ResponseEntity deleteWord(Integer id) {
        return wordService.removeById(id) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("myWord/query")
    public ResponseEntity queryMyWordRecord(HttpServletRequest request, Page<Word> page, Word word) {
        return ResponseEntity.success(wordService.findMyWordPageByCondition(page, obtainRequestSetWordUserId(request, word)));
    }

    private Word obtainRequestSetWordUserId(HttpServletRequest request, Word word) {
        User currentUser = securityFeign.findUserByRequest(request);
        if (Objects.nonNull(currentUser)) word.setUserId(currentUser.getId());
        return word;
    }
}
