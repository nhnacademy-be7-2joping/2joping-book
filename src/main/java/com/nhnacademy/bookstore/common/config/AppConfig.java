package com.nhnacademy.bookstore.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AppConfig 클래스는 Spring의 설정 클래스입니다.
 * 이 클래스는 AOP(Aspect-Oriented Programming) 기능을 활성화하고,
 * 애플리케이션 전반에 걸쳐 Aspect를 적용할 수 있도록 설정합니다.
 *
 * @author Luha
 * @version 1.0
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}