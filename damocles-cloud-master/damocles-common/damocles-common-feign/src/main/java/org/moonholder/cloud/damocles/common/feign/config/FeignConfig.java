package org.moonholder.cloud.damocles.common.feign.config;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import feign.*;
import feign.hystrix.SetterFactory;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

import static java.util.concurrent.TimeUnit.SECONDS;

@Configuration
public class FeignConfig extends FeignClientProperties.FeignClientConfiguration {
    /**
     * 配置请求重试
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(200, SECONDS.toMillis(2), 10);
    }


    /**
     * 设置请求超时时间
     * 默认
     * public Options() {
     * this(10 * 1000, 60 * 1000);
     * }
     */
    @Bean
    Request.Options feignOptions() {
        return new Request.Options(60 * 1000, 60 * 1000);
    }


    /**
     * 打印请求日志
     *
     * @return
     */
    @Bean
    public Logger.Level multipartLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 配置动态地址
     * 请求头参数透传
     *
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                //透传请求header参数
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        requestTemplate.header(name, values);
                    }
                }
            }
        };
    }

    /**
     * hystrix 相关配置，feign默认集成
     *
     * @return
     */
    @Bean
    public SetterFactory setterFactory() {
        SetterFactory setterFactory = new SetterFactory() {
            @Override
            public HystrixCommand.Setter create(Target<?> target, Method method) {

                String groupKey = target.name();
                String commandKey = Feign.configKey(target.type(), method);

                HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
//                        //设置统计指标60秒为一个时间窗口
//                        .withMetricsRollingStatisticalWindowInMilliseconds(1000 * 60)
//                        //超过80%失败率
//                        .withCircuitBreakerErrorThresholdPercentage(80)
//                        //操作5个开启短路器
//                        .withCircuitBreakerRequestVolumeThreshold(5)
                        //设置断路器的开启时间为60秒
//                        .withCircuitBreakerSleepWindowInMilliseconds(1000 * 60)
                        //设置信号量隔离
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE);

                return HystrixCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                        .andCommandPropertiesDefaults(setter);
            }
        };
        return setterFactory;
    }

}
