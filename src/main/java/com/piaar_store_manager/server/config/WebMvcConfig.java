package com.piaar_store_manager.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
    @Value("${file.upload-dir}")
    String assetsPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // TODO Auto-generated method stub
        registry
            .addResourceHandler("/uploads/**")
            .addResourceLocations("file://"+assetsPath+"/")
            .setCachePeriod(0);
    }
}
