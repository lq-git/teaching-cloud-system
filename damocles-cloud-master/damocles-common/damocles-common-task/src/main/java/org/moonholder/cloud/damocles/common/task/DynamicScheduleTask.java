package org.moonholder.cloud.damocles.common.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author moonholder
 * @Description // 动态定时任务
 * @Date 15:28 2020/12/29
 */
@Component
public class DynamicScheduleTask {
    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    private ScheduledFuture<?> scheduledFuture;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 开启定时任务
     * 在指定时间执行一次定时任务
     * @param task 指定任务
     * @param startDate 指定时间
     */
    public void startTask(Runnable task, Date startDate) {
        scheduledFuture = threadPoolTaskScheduler.schedule(task, startDate);
        logger.info("定时任务开启，{}:开始执行", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startDate));
    }

    /**
     * 关闭当前定时任务
     */
    public void stopTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
    }
}
