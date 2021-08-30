package org.moonholder.cloud.damocles.daily.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.Response;
import org.moonholder.cloud.damocles.common.core.entity.Daily;
import org.moonholder.cloud.damocles.common.core.entity.User;
import org.moonholder.cloud.damocles.common.core.entity.vo.ResponseEntity;
import org.moonholder.cloud.damocles.daily.feign.FileFeign;
import org.moonholder.cloud.damocles.daily.feign.SecurityFeign;
import org.moonholder.cloud.damocles.daily.service.IDailyService;
import org.moonholder.cloud.damocles.daily.util.GenerateDoc;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("daily")
public class DailyController {

    @Resource
    private IDailyService dailyService;
    @Resource
    private SecurityFeign securityFeign;
    @Resource
    private FileFeign fileFeign;

    @GetMapping("record/read")
    public ResponseEntity queryRecord(HttpServletRequest request, Daily daily, Page<Daily> page) {
        User user = securityFeign.findUserByRequest(request);
        daily.setUserId(user.getId());
        return ResponseEntity.success(dailyService.findDailyPageByCondition(page, daily));
    }

    @PostMapping("record/create")
    public ResponseEntity createRecord(HttpServletRequest request, @RequestBody Daily daily) {
        User user = securityFeign.findUserByRequest(request);
        daily.setUserId(user.getId());
        daily.setStatus("draft");
        return dailyService.save(daily) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @PostMapping("record/update")
    public ResponseEntity updateRecord(@RequestBody Daily daily) {
        return dailyService.updateById(daily) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @DeleteMapping("record/delete")
    public ResponseEntity deleteRecord(Integer dailyId) {
        return dailyService.removeById(dailyId) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("checkRecord")
    public ResponseEntity checkRecord(Page<Daily> page) {
        return ResponseEntity.success(dailyService.page(page, Wrappers.lambdaQuery(Daily.class).eq(Daily::getStatus, "submitted")));
    }

    @PostMapping("checkRecord/check")
    public ResponseEntity checkRecord(@RequestBody Daily daily) {
        return dailyService.updateById(daily) ? ResponseEntity.success() : ResponseEntity.error();
    }

    @GetMapping("list/query")
    public ResponseEntity checkRecord(Daily daily, Page<Daily> page) {
        return ResponseEntity.success(dailyService.findDailyPageByCondition(page, daily));
    }

    @PostMapping("list/doc")
    public void dailyDoc(@RequestBody Daily daily, HttpServletResponse response) {
        String fileName = GenerateDoc.generating(daily);
        InputStream inputStream = null;
        try {
            Response serviceResponse = fileFeign.download(fileName);
            Response.Body body = serviceResponse.body();
            inputStream = body.asInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            response.setHeader("Content-Disposition", serviceResponse.headers().get("Content-Disposition").toString().replace("[","").replace("]",""));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream());
            int length = 0;
            byte[] temp = new byte[1024 * 10];
            while ((length = bufferedInputStream.read(temp)) != -1) {
                bufferedOutputStream.write(temp, 0, length);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            bufferedInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("userRecord")
    public ResponseEntity userRecord(User user, Page<User> page) {
        return ResponseEntity.success(dailyService.findDailySubmitRecord(page, user));
    }
}
