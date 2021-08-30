package org.moonholder.cloud.damocles.security.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.moonholder.cloud.damocles.common.core.entity.Dict;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author MoonHolder
 * @since 2020-12-19
 */
public interface IDictService extends IService<Dict> {

    List<Dict> findDictListByCondition(Dict dict);

    List<Dict> findDictList();
}
