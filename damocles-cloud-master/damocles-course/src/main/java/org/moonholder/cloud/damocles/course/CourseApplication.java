package org.moonholder.cloud.damocles.course;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName CourseApplication
 * @Description TODO
 * @Author qiang
 * @Date 2021/8/27 15:32
 * @Version 1.0
 **/
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class,args);
    }
}
