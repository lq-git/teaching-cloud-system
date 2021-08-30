package org.moonholder.cloud.damocles.daily.util;


import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.moonholder.cloud.damocles.common.core.annotation.TemplatePlaceholder;
import org.moonholder.cloud.damocles.common.core.util.SysUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author moonholder
 * @Description //生成日报
 * @Date 9:50 2021/1/4
 */
public class GenerateDoc {

    public static String generating(Object target) {
        StringBuilder sb = new StringBuilder("日报-");
        Map<String, Object> dataMap = new HashMap<>();
        Class<?> targetClass = target.getClass();
        Field[] fields = targetClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(TemplatePlaceholder.class)) {
                try {
                    Object value = field.get(target);
                    String fieldName = field.getName();
                    if (fieldName.equalsIgnoreCase("author")) {
                        sb.append(value);
                    } else if (fieldName.equalsIgnoreCase("time")) {
                        sb.append("-".concat(value.toString()).concat(".doc"));
                    }
                    dataMap.put(fieldName, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        Configuration configuration = new Configuration(new Version("2.3.0"));
        configuration.setDefaultEncoding("utf-8");
        try {
            /* Configuration的路径设置 - 基于文件系统的方式在生产环境会找不到卷标，应此采用类路径加载的方式 */
//            configuration.setClassForTemplateLoading(GenerateDoc.class, "/static/templates/");
            configuration.setDirectoryForTemplateLoading(new File(SysUtil.CommonPath.TEMPLATE.value));
            File outFile = new File(SysUtil.CommonPath.DOC.value, sb.toString());
            Template template = configuration.getTemplate("template.ftl", "utf-8");
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8), 10240);
            template.process(dataMap, out);
            out.close();
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return SysUtil.DOC_PATH.concat("/").concat(sb.toString());
    }
}
