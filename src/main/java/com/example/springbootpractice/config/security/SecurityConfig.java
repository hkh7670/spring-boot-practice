package com.example.springbootpractice.config.security;

import static com.example.springbootpractice.model.enums.Role.ROLE_ADMIN;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public static final String[] PERMIT_ALL_URI = {
        "/h2-console/**",
        "/swagger.html", "/swagger-ui/**",
        "/api-docs", "/api-docs/**",
        "/api/v1/user/signup", "/api/v1/user/login",
    };

    private static final String[] ADMIN_API_URI = {
        "/api/v1/webtoon/*/coin", "api/v1/user/adult-webtoon-view/**"
    };


    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // authenticationManager를 Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS))  // 토큰 기반 인증이므로 세션 사용 안함
            .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PERMIT_ALL_URI).permitAll()
                .requestMatchers(ADMIN_API_URI).hasAuthority(ROLE_ADMIN.name())
                .anyRequest().authenticated()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(this.accessDeniedHandler) // 403 에러 처리
                .authenticationEntryPoint(this.authenticationEntryPoint) // 401 에러 처리
            )
            // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣음
            .addFilterBefore(
                new JwtAuthenticationFilter(this.jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

}
