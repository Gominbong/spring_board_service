package com.example.myproject;

import com.example.myproject.interceptor.LogInterceptor;
import com.example.myproject.interceptor.LoginCheckInterceptor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        CacheControl cacheControl = CacheControl
                .maxAge(600, TimeUnit.SECONDS)
                .mustRevalidate();

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/templates/")
                .setCacheControl(cacheControl);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {


        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/signup", "/login", "/css/*", "/js/*", "/icon/*", "/signupComplete",
                        "/error", "/api/**"  );

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/commentEdit", "/add", "/buyList", "/sellList", "/myInfo", "/addMusicList", "/cart",
                        "/updateMusicList")
                .excludePathPatterns("/", "/signup", "/login", "/css/*", "/js/*", "/icon/*", "/signupComplete",
                        "/error", "/api/**" );

    }

}
