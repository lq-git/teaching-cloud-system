package org.moonholder.cloud.damocles.common.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class ScheduleTaskAutoConfiguration {

    @Bean
    public DynamicScheduleTask dynamicScheduleTask() {
        return new DynamicScheduleTask();
    }
}
