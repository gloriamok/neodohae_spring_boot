package com.neodohae_spring_boot.neodohae_spring_boot.config;

import com.neodohae_spring_boot.neodohae_spring_boot.security.JwtAuthenticationEntryPoint;
import com.neodohae_spring_boot.neodohae_spring_boot.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    // UserDetailsService를 구현한 CustomUserDetailsService를 사용하기 위해
    // UserDetailsService를 의존성 주입
    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtAuthenticationFilter authenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    // AuthenticationManager를 사용해서 사용자 인증 요청을 처리(사용자의 자격 증명 관리)하기 위해
    // AuthenticationManager 빈 생성
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // encode plaintext password to one-way hash using BCrypt algorithm
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityFilterChain은 Spring Security에서 HTTP 요청을 처리하는 필터들의 체인
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 기능 비활성화
        // 모든 HTTP 요청에 대해 인증이 필요
        //  HTTP Basic 인증 방식을 사용하도록 설정
        http.csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        //authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET, "/api/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/**").permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                                .anyRequest().authenticated()
                );//.httpBasic(Customizer.withDefaults());
        // SecurityFilterChain을 빌드하고 반환
        return http.build();
    }
}