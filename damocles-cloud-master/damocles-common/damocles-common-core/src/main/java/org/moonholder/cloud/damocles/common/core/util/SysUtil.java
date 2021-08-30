package org.moonholder.cloud.damocles.common.core.util;

import java.io.File;

/**
 * @Author moonholder
 * @Description //系统字典
 * @Date 11:35 2020/11/24
 */
public class SysUtil {
    public static final String ROOT_PATH = System.getProperty("user.dir").replaceAll("\\\\", "/").concat("/static/");
    public static final String IMG_PATH = "images";
    public static final String DOC_PATH = "doc";
    public static final String TEMPLATE_PATH = "templates";


    public enum CommonPath {
        IMG(IMG_PATH),
        DOC(DOC_PATH),
        TEMPLATE(TEMPLATE_PATH);
        public String value;

        CommonPath(String value) {
            String path = ROOT_PATH.concat(value).concat("/");
            File file = new File(path);
            file.mkdirs();
            this.value = path;
        }
    }

    public static String startWithPathReplace(String contentType, String fileName) {
        if (contentType == null || contentType.equals("")) return fileName;
        String path = fileName;
        contentType = contentType.toLowerCase();
        if (contentType.startsWith("image")) {
            path = IMG_PATH.concat("/").concat(fileName);
        } else if (contentType.contains(DOC_PATH)) {
            path = DOC_PATH.concat("/").concat(fileName);
        }
        return path;
    }
}
