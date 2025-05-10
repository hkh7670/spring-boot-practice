package com.example.springbootpractice.config;

import com.example.springbootpractice.config.log.RequestIdFilter;
import com.example.springbootpractice.config.log.ResponseHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
        FilterRegistrationBean<RequestIdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestIdFilter());
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE); // 가장 먼저 실행
        return registration;
    }

    @Bean
    public FilterRegistrationBean<ResponseHeaderFilter> responseHeaderFilter() {
        FilterRegistrationBean<ResponseHeaderFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ResponseHeaderFilter());
        registration.setOrder(Ordered.LOWEST_PRECEDENCE); // 가장 나중에 실행
        return registration;
    }
}