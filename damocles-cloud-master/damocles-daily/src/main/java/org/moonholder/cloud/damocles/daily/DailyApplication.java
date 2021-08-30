package org.moonholder.cloud.damocles.daily;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DailyApplication {
    public static void main(String[] args) {
        SpringApplication.run(DailyApplication.class, args);
    }
}
