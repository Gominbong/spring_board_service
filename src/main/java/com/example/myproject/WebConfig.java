package com.example.myproject;

import com.example.myproject.interceptor.LogInterceptor;
import com.example.myproject.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/signup", "/login", "/css/*", "/js/*", "/icon/*", "/signupComplete",
                        "/error"  );

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(3)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/signup", "/login", "/css/*", "/js/*", "/icon/*", "/signupComplete",
                        "/error" );

    }
}
