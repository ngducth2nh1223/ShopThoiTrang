package com.poly.config;

import com.poly.filter.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Áp dụng Interceptor cho các đường dẫn cần bảo vệ
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/admin/**", "/checkout")
                .excludePathPatterns("/assets/**", "/static/**", "/login", "/logout");
    }
}