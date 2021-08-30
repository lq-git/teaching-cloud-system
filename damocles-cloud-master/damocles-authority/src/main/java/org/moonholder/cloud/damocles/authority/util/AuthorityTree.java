package org.moonholder.cloud.damocles.authority.util;


import org.moonholder.cloud.damocles.common.core.entity.Authority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author moonholder
 * @Description //权限树
 * @Date 16:47 2020/12/21
 */
@Component
public class AuthorityTree {
    private List<Authority> protoList = null; // 原型数据
    private int[] excludeTypes;

    public List<Authority> generateAuthorityTree(List<Authority> protoList, int... excludeType) {
        this.protoList = protoList;
        this.excludeTypes = excludeType;
        List<Authority> treeList = new ArrayList<>(); // 树形结构集合
        for (Authority authority : protoList) {
            if (authority.getPid() == 0) {
                treeList.add(loadingData(authority));
            }
        }
        return treeList;
    }

    private Authority loadingData(Authority item) {
        return item.setChildren(getTreeChild(item));
    }

    private List<Authority> getTreeChild(Authority parentItem) {
        List<Authority> childList = new ArrayList<>();
        for (Authority item : protoList) {
            if (!isExclude(item) && item.getPid().intValue() == parentItem.getId() && item.getType() - parentItem.getType() == 1) {
                childList.add(loadingData(item));
            }
        }
        return childList;
    }

    /**
     * 是否为排除项
     *
     * @param authority 权限
     * @return
     */
    private boolean isExclude(Authority authority) {
        if (excludeTypes.length != 0) {
            for (int excludeType : excludeTypes) {
                if (authority.getType() == excludeType) return true;
            }
        }
        return false;
    }

}
