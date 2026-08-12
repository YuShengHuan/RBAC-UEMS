package com.ru.app.common.config;


import com.ru.app.common.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    // 将拦截器注册为 Bean
    private final TokenInterceptor tokenInterceptor;
    public WebConfig(
            TokenInterceptor tokenInterceptor
    ){
        this.tokenInterceptor=tokenInterceptor;
    }
    // 配置拦截规则
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .excludePathPatterns(
                        "/api/login",
                        "/api/forgot-password"

                ).addPathPatterns(
                        "/api/**"
                );
    }
    @Value("${cookie.allowed-origins}")
    private String allowedOrigins;
    //配置cookie的携带配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins) // 前端域名
                .allowCredentials(true) // 允许携带Cookie
                .allowedMethods("*")
                .maxAge(3600);
    }
    @Value("${file.upload.path}")
    private String uploadPath;

    // 从配置文件注入访问URL的路径模式
    @Value("${file.upload.access-path}")
    private String accessPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(accessPath)
                .addResourceLocations("file:" + uploadPath); // 注意前缀 "file:" 表示本地文件系统
    }
}
