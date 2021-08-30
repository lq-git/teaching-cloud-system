package org.moonholder.cloud.damocles.file.service;


import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface FileTransferService {
    ResponseEntity upload(MultipartFile[] file);

    void download(String fileName, HttpServletResponse response);
}
