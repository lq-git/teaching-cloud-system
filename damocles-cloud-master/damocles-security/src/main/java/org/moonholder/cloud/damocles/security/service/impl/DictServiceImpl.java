package org.moonholder.cloud.damocles.security.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.moonholder.cloud.damocles.common.core.entity.Dict;
import org.moonholder.cloud.damocles.security.mapper.DictMapper;
import org.moonholder.cloud.damocles.security.service.IDictService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
@Service
@CacheConfig(cacheNames = "dict")
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements IDictService {

    @Override
    @Cacheable(key = "#root.methodName")
    public List<Dict> findDictList() {
        return list();
    }

    @Override
    public List<Dict> findDictListByCondition(Dict dict) {
        List<Dict> dictList = new LambdaQueryChainWrapper<>(baseMapper).eq(Objects.nonNull(dict.getId()), Dict::getId, dict.getId())
                .eq(StringUtils.hasLength(dict.getType()), Dict::getType, dict.getType())
                .eq(StringUtils.hasLength(dict.getCode()), Dict::getCode, dict.getCode())
                .like(StringUtils.hasLength(dict.getText()), Dict::getText, dict.getText()).list();

        return dictList;
    }
}
