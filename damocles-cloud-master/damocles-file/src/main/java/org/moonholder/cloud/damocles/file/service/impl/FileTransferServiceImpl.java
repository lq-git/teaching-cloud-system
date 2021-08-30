package org.moonholder.cloud.damocles.file.service.impl;

import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.common.core.util.SysUtil;
import org.moonholder.cloud.damocles.common.core.util.Toolkit;
import org.moonholder.cloud.damocles.file.service.FileTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FileTransferServiceImpl implements FileTransferService {

    private Logger logger = LoggerFactory.getLogger(FileTransferServiceImpl.class);

    @Override
    public ResponseEntity upload(MultipartFile[] files) {
        List<String> succeedUploadPath = new ArrayList<>();
        for (MultipartFile file : files) {
            if (Objects.nonNull(file)) {
                File path = new File(SysUtil.CommonPath.IMG.value);
                if (path.exists() || path.mkdirs()) {
                    try {
                        String filename = file.getOriginalFilename();
                        filename = Objects.requireNonNull(filename).substring(filename.lastIndexOf("."));
                        filename = Toolkit.getUUIDToken().concat(filename);
                        path = new File(path, filename);
                        file.transferTo(path);
                        filename = SysUtil.startWithPathReplace(file.getContentType(), filename);
                        succeedUploadPath.add(filename);
                        logger.info(path.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity.error("文件上传失败");
                    }
                }
            }
        }
        return ResponseEntity.success(succeedUploadPath);
    }

    @Override
    public void download(String path, HttpServletResponse response) {
        File file = new File(SysUtil.ROOT_PATH, path);
        try {
            if (file.exists()) {
                byte[] data = FileCopyUtils.copyToByteArray(file);
                ServletOutputStream outputStream = response.getOutputStream();
                String fileName = path.substring(path.lastIndexOf("/") + 1);
                response.reset();
                response.setContentType("application/octet-stream");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
                outputStream.write(data);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
