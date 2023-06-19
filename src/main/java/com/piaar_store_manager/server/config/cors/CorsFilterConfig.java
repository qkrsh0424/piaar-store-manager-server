package com.piaar_store_manager.server.config.cors;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig{
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // config.addAllowedOrigin("http://localhost:3000");
        // config.addAllowedOrigin("http://localhost:3001");
        // config.addAllowedOrigin("http://localhost:8081");
        // config.addAllowedOrigin("http://localhost:5000");
        // config.addAllowedOrigin("http://www.piaar.co.kr");
        // config.addAllowedOrigin("https://www.piaar.co.kr");
        // config.addAllowedOrigin("http://dev.www.piaar.co.kr");
        // config.addAllowedOrigin("https://dev.www.piaar.co.kr");
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("PATCH");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.setExposedHeaders(Arrays.asList("Authorization"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
