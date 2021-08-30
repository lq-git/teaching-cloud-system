package org.moonholder.cloud.damocles.file.controller;

import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.file.service.FileTransferService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
public class FileController {
    @Resource
    private FileTransferService fileTransferService;

    @RequestMapping(value = "upload",method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity upload(@RequestPart(value = "files") MultipartFile[] files) {
        return fileTransferService.upload(files);
    }

    @GetMapping("download")
    public void download(String path, HttpServletResponse response) {
        fileTransferService.download(path, response);
    }
}
