package org.moonholder.cloud.damocles.security.fallback;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.security.feign.FileFeign;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
@Component
@Slf4j
public class FileFallback implements FileFeign {
    @Override
    public ResponseEntity upload(MultipartFile[] files) {
        log.info("文件上传降级");
        return ResponseEntity.error("上传失败");
    }

    @Override
    public Response download(String path) {
        log.info("文件下载降级");
        return null;
    }
}
