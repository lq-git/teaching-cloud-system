package org.moonholder.cloud.damocles.file.config;

import org.moonholder.cloud.damocles.common.core.util.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info(SysUtil.CommonPath.IMG.value);
        registry.addResourceHandler("/images/**").addResourceLocations("file:".concat(SysUtil.CommonPath.IMG.value));
    }
}
