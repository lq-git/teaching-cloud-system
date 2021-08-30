package org.moonholder.cloud.damocles.daily.feign;

import feign.Response;
import org.moonholder.cloud.damocles.common.core.constant.ServiceNameConstant;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.common.feign.config.FeignConfig;
import org.moonholder.cloud.damocles.daily.fallback.FileFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = ServiceNameConstant.FILE, fallback = FileFallback.class,configuration = FeignConfig.class)
public interface FileFeign {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity upload(@RequestPart(value = "files") MultipartFile[] files);

    @GetMapping(value = "/download", consumes = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
    Response download(@RequestParam("path") String path);
}
