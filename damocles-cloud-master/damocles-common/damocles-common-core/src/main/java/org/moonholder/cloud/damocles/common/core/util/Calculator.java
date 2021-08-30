package org.moonholder.cloud.damocles.common.core.util;

import java.util.*;

/**
 * @Author moonholder
 * @Description //TODO 计算工具类
 * @Date 16:02 2020/12/31
 */
public class Calculator {

    /**
     * 计算比较集合在主集合中不存在的元素 - 差异
     *
     * @param mainArray    主集合
     * @param compareArray 比较集合
     * @return 差异元素集合
     */
    public static List<Integer> calcDifference(Integer[] mainArray, Integer[] compareArray) {
        if (isArrayIsNull(compareArray)) return new ArrayList<>();
        else if (isArrayIsNull(mainArray)) return Arrays.asList(compareArray);
        List<Integer> diffList = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        int offset = 8;
        for (Integer num : mainArray) {
            int idx = num / offset;
            if (!map.containsKey(idx)) map.put(idx, new ArrayList<>());
            map.get(idx).add(num);
        }
        for (Integer num : compareArray) {
            int idx = num / offset;
            if (map.get(idx) == null || !map.get(idx).contains(num)) diffList.add(num);
        }
        return diffList;
    }

    private static boolean isArrayNonNull(Integer[] array) {
        return array != null && array.length > 0;
    }

    private static boolean isArrayIsNull(Integer[] array) {
        return !isArrayNonNull(array);
    }

}
