package com.xiaozou.security.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author : wh
 * @date : 2025/2/19 15:29
 * @description:
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 使用 AntPathRequestMatcher 替代字符串路径
                .requestMatchers(new AntPathRequestMatcher("/")).permitAll() // 允许所有人访问根路径
                .requestMatchers(new AntPathRequestMatcher("/public")).permitAll() // 公开页面
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll() // 公开页面
                .requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER") // 需要USER角色
                .requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN") // 需要ADMIN角色
                .anyRequest().authenticated() // 其他请求需认证
            )
            .formLogin(form -> form
                .loginPage("/login") // 自定义登录页路径
                .permitAll()
                .defaultSuccessUrl("/home", true) // 登录成功后强制跳转到/home
            )
            
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout") // 注销后跳转地址
                .permitAll()  // 允许所有人访问注销端点
            )
            .userDetailsService(userDetailsService)
        .headers(headers -> headers.frameOptions().disable()) // 允许H2控制台
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            );
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
