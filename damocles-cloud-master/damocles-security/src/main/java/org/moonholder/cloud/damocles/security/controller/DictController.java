package org.moonholder.cloud.damocles.security.controller;


import org.moonholder.cloud.damocles.common.core.entity.Dict;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.security.service.IDictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource
    private IDictService dictService;

    @GetMapping("list")
    public ResponseEntity list() {
        return ResponseEntity.success(dictService.findDictList());
    }

    @GetMapping("query")
    public ResponseEntity query(Dict dict) {
        return ResponseEntity.success(dictService.findDictListByCondition(dict));
    }
}
