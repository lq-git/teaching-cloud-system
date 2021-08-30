package org.moonholder.cloud.damocles.common.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author moonhodler
 * @Description //常用工具类
 * @Date 14:43 2020/11/24
 */
public class Toolkit {
    public static boolean nonNull(Object obj) {
        return obj != null && obj != "" && !obj.equals("");
    }

    public static boolean isNull(Object obj) {
        return obj == null || obj == "" || obj.equals("");
    }

    /**
     * 字符转字段类型
     *
     * @param obj
     * @param type
     * @return
     */
    public static Object stringToFieldType(Object obj, String type) {
        if (!obj.getClass().getSimpleName().equals(type)) {
            String val = obj.toString();
            if (type.equals(Integer.class.getSimpleName())) {
                obj = Integer.parseInt(val);
            } else if (type.equals(Double.class.getSimpleName())) {
                obj = Double.parseDouble(val);
            } else if (type.equals(Date.class.getSimpleName())) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    obj = sdf.parse(val);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (type.equals(Timestamp.class.getSimpleName())) {
                obj = Timestamp.valueOf(val);
            } else {
                obj = val;
            }
        }
        return obj;
    }

    /**
     * 对象字段属性转字符串
     *
     * @param obj
     * @return
     */
    public static String fieldTypeToString(Object obj) {
        String fileType = obj.getClass().getSimpleName();
        if (fileType.equals(Timestamp.class.getSimpleName())
                || fileType.equals(Date.class.getSimpleName())) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(obj);
        }
        return obj.toString();
    }

    /**
     * 获取uuid
     *
     * @return
     */
    public static String getUUIDToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
